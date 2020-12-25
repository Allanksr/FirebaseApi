package allanksr.com.firebase.authentication.mvpYahoo

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


class YahooImp (
        private var yahooView: YahooView,
        private var preventExit: PreventExit
): YahooPresenter {
    private var preferences = Prefs()
    private lateinit var scopes: ArrayList<String>
    private var cannotExit = false
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth(activity: Activity) {
        if (firebaseInstance().currentUser != null) {
            for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                 if(profile.providerId.contains("yahoo.com")){
                  Log.d("providerId", profile.providerId)
                    yahooView.onSuccess(
                            preferences.getStringSP(activity, "yahooName"),
                            preferences.getStringSP(activity, "yahooImage")
                    )
                 }
            }
        }else{
            yahooView.onSignOut()
        }
    }


    override fun yahooLogin(activity: Activity) {
        yahooView.waitResult()
        val oAuthProvider = OAuthProvider.newBuilder("yahoo.com")
        //oAuthProvider.addCustomParameter("login", "somemail@gmail.com")
        //oAuthProvider.addCustomParameter("prompt", "login")
        scopes = ArrayList()
        scopes.add("mail-r")
        scopes.add("sdct-w")
        //oAuthProvider.scopes = scopes
        firebaseInstance().startActivityForSignInWithProvider(activity, oAuthProvider.build())
                .addOnSuccessListener {
                    val str = it.additionalUserInfo?.profile
                    val json = JSONObject(str!!)

                    val imageProfile = json.getString("picture")
                    val name = json.getString("name")

                    preferences.setStringSP(activity, "yahooName", name)
                    yahooView.onSuccess(name, imageProfile)
                    saveImage(imageProfile, activity)

                     Log.d("yahooSuccess:", "$json")
                }
                .addOnFailureListener {
                    cannotExit = if(it.message ==  activity.getString(R.string.operation_was_canceled)){
                        yahooView.onFail(it.message!!)
                        false
                    }else{
                        val error = String.format(
                                activity.getString(R.string.accountAlreadyExists)+": %s ",
                                activity.getString(R.string.findProvider))
                        yahooView.onFail(error)
                        yahooView.findProvider()
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
                    File(storageDir, "yahoo.png").apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        currentPhotoPath = absolutePath
                        // Log.d("currentPhotoPath :", currentPhotoPath)
                    }.also {
                        it.also {
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                                val permissionTemp =  Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                context.grantUriPermission(context.packageName, Uri.fromFile(it), permissionTemp)
                                val uriPermission = Uri.fromFile(it)
                                preferences.setStringSP(context, "yahooImage", "$uriPermission")
                                // Log.d("uriPermission :", "$uriPermission")
                            }else{
                                preferences.setStringSP(context, "yahooImage", "$it")
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
            yahooView.onFail("MalformedURLException $mue")
        } catch (ioe: IOException) {
            //Log.d("IOException :", "$ioe")
            yahooView.onFail("IOException $ioe")
        } catch (se: SecurityException) {
            //Log.d("SecurityException :", "$se")
            yahooView.onFail("SecurityException $se")
        }

    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            yahooView.onSignOut()
        }
    }

    override fun findProvider(activity: Activity, email: String) {
        yahooView.waitResult()
        cannotExit = true
        if(TextUtils.isEmpty(email)){
            yahooView.onFail(activity.getString(R.string.fill_field)+" | "+activity.getString(R.string.findProvider))
            cannotExit = false
        }else{
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{ it ->
                it.addOnSuccessListener {
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${it.signInMethods}")
                    yahooView.onFail(error)
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