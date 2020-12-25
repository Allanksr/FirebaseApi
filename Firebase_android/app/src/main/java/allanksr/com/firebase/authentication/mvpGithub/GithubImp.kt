package allanksr.com.firebase.authentication.mvpGithub

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserInfo
import org.json.JSONObject
import java.io.*
import java.net.MalformedURLException
import java.net.URL

class GithubImp(
        private var githubView: GithubView,
        private var preventExit: PreventExit
): GithubPresenter {
    private var preferences = Prefs()
    private var cannotExit = false
    private lateinit var scopes: ArrayList<String>
    override fun firebaseAuth(activity: Activity) {
        if (firebaseInstance().currentUser != null) {
            for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                if(profile.providerId.contains("github.com")){
                Log.d("providerId", profile.providerId)
                    githubView.onSuccess(
                            preferences.getStringSP(activity, "githubName"),
                            preferences.getStringSP(activity, "githubImage")
                    )
                }
            }
        }else{
            githubView.onSignOut()
        }
    }

    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

        override fun githubLogin(activity: Activity) {
            githubView.waitResult()
            cannotExit = true
            val oAuthProvider = OAuthProvider.newBuilder("github.com")
            //oAuthProvider.addCustomParameter("login", "somemail@gmail.com")
            scopes = ArrayList()
            scopes.add("user:email")
            oAuthProvider.scopes = scopes
            firebaseInstance().startActivityForSignInWithProvider(activity, oAuthProvider.build())
                .addOnSuccessListener {
                    val str = it.additionalUserInfo?.profile
                    val json = JSONObject(str!!)

                    val imageProfile = json.getString("avatar_url")
                    val name = json.getString("name")

                    preferences.setStringSP(activity, "githubName", name)
                    githubView.onSuccess(name, imageProfile)
                    saveImage(imageProfile, activity)
                    cannotExit = false
                   // Log.d("githubSuccess:", "$json")
                }
                .addOnFailureListener {
                    cannotExit = if(it.message ==  activity.getString(R.string.operation_was_canceled)){
                        githubView.onFail(it.message!!)
                        false
                    }else{
                        val error = String.format(
                                activity.getString(R.string.accountAlreadyExists)+": %s ",
                                activity.getString(R.string.findProvider))
                        githubView.onFail(error)
                        githubView.findProvider()
                        false
                    }
                }
        }

    private lateinit var currentPhotoPath: String
    private fun saveImage(url: String, context: Context) {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val u = URL(url)
            val mInputStream: InputStream = u.openStream()
            val dis = DataInputStream(mInputStream)
            val buffer = ByteArray(1024)
            var length: Int

            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            val fos = FileOutputStream(
                    File(storageDir, "github.png").apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        currentPhotoPath = absolutePath
                        // Log.d("currentPhotoPath :", currentPhotoPath)
                    }.also {
                        it.also {
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                                val permissionTemp =  Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                context.grantUriPermission(context.packageName, Uri.fromFile(it), permissionTemp)
                                val uriPermission = Uri.fromFile(it)
                                preferences.setStringSP(context, "githubImage", "$uriPermission")
                                // Log.d("uriPermission :", "$uriPermission")
                                // galleryAddPic(context)
                            }else{
                                // galleryAddPic(context)
                                preferences.setStringSP(context, "githubImage", "$it")
                                //Log.d("uriPermission :", "$it")
                            }
                        }
                    }
            )

            while (dis.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        } catch (mue: MalformedURLException) {
            //Log.d("MalformedURLException :", "$mue")
            githubView.onFail("MalformedURLException $mue")
        } catch (ioe: IOException) {
            //Log.d("IOException :", "$ioe")
            githubView.onFail("IOException $ioe")
        } catch (se: SecurityException) {
            //Log.d("SecurityException :", "$se")
            githubView.onFail("SecurityException $se")
        }

    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            githubView.onSignOut()
        }
    }

    override fun findProvider(activity: Activity, email: String) {
        githubView.waitResult()
        cannotExit = true
        if(TextUtils.isEmpty(email)){
            githubView.onFail(activity.getString(R.string.fill_field)+" | "+activity.getString(R.string.findProvider))
            cannotExit = false
        }else{
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{ it ->
                it.addOnSuccessListener {
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${it.signInMethods}")
                    githubView.onFail(error)
                }
                cannotExit = false
            }
        }
    }

    override fun exitActivity() {
        if(cannotExit){
            preventExit.waitToExit()
        }else{
            preventExit.exitActivity()
        }
    }

}