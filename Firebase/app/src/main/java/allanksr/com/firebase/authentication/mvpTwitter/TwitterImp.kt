package allanksr.com.firebase.authentication.mvpTwitter

import allanksr.com.firebase.Prefs
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import com.google.firebase.auth.*
import org.json.JSONObject
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.text.TextUtils

class TwitterImp(
    private var twitterView: TwitterView,
    private var preventExit: PreventExit
): TwitterPresenter {
    private var preferences = Prefs()
    private var cannotExit = false
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth(activity: Activity) {
        if (firebaseInstance().currentUser != null) {
            for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                if(profile.providerId.contains("twitter.com")){
                Log.d("providerId", profile.providerId)
                    twitterView.onSuccess(
                        preferences.getStringSP(activity, "twitterName"),
                        preferences.getStringSP(activity, "twitterImage")
                    )
                }
              }
            }else{
                twitterView.onSignOut()
            }
        }



    override fun twitterLogin(activity: Activity) {
            twitterView.waitResult()
            cannotExit = true
            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
              firebaseInstance().startActivityForSignInWithProvider(activity, oAuthProvider.build())
                 .addOnSuccessListener {
                        val str = it.additionalUserInfo?.profile
                        val json = JSONObject(str!!)
                        var image = json.getString("profile_image_url_https")
                        if(image.contains("_normal")){
                            image = image.replace("_normal", "")
                        }
                        val userName = json.getString("name")

                        val twitterCredential =  (it.credential as OAuthCredential).accessToken
                        Log.d("twitterCredential:", twitterCredential)

                        preferences.setStringSP(activity, "twitterName", userName)
                        twitterView.onSuccess(userName, image)
                        saveImage(image, activity)

                    }
                 .addOnFailureListener {
                     cannotExit = if(it.message ==  activity.getString(R.string.operation_was_canceled)){
                         twitterView.onFail(it.message!!)
                         false
                     }else{
                         val error = String.format(
                             activity.getString(R.string.accountAlreadyExists)+": %s ",
                             activity.getString(R.string.findProvider))
                         twitterView.onFail(error)
                         twitterView.findProvider()
                         false
                     }

                }


        }

    override fun findProvider(activity: Activity, email: String) {
        twitterView.waitResult()
        cannotExit = true
        if(TextUtils.isEmpty(email)){
            twitterView.onFail(activity.getString(R.string.fill_field)+" | "+activity.getString(R.string.findProvider))
            cannotExit = false
        }else{
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{
                it.addOnSuccessListener {queryResult->
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${queryResult.signInMethods}")
                    twitterView.onFail(error)
                }
                cannotExit = false
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
                File(storageDir, "twitter.png").apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    // Log.d("currentPhotoPath :", currentPhotoPath)
                }.also {
                    it.also {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            val permissionTemp =
                                Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            context.grantUriPermission(
                                context.packageName,
                                Uri.fromFile(it),
                                permissionTemp
                            )
                            val uriPermission = Uri.fromFile(it)
                            preferences.setStringSP(context, "twitterImage", "$uriPermission")
                            // Log.d("uriPermission :", "$uriPermission")
                        } else {
                            preferences.setStringSP(context, "twitterImage", "$it")
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
            twitterView.onFail("MalformedURLException $mue")
        } catch (ioe: IOException) {
            //Log.d("IOException :", "$ioe")
            twitterView.onFail("IOException $ioe")
        } catch (se: SecurityException) {
            //Log.d("SecurityException :", "$se")
            twitterView.onFail("SecurityException $se")
        }
        cannotExit = false
    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            twitterView.onSignOut()
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