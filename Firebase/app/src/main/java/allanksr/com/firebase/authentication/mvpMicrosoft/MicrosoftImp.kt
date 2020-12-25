package allanksr.com.firebase.authentication.mvpMicrosoft

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserInfo
import org.json.JSONObject
import java.io.*

class MicrosoftImp(
        private var microsoftView: MicrosoftView,
        private var preventExit: PreventExit
): MicrosoftPresenter {
    private var preferences = Prefs()
    private lateinit var scopes: ArrayList<String>
    private var cannotExit = false
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth(activity: Activity) {
        if (firebaseInstance().currentUser != null) {
            for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                if(profile.providerId.contains("microsoft.com")){
                Log.d("providerId", profile.providerId)
                    microsoftView.onSuccess(
                        preferences.getStringSP(activity, "microsoftName"),
                        preferences.getStringSP(activity, "microsoftImage")
                    )
                 }
            }
        }else{
            microsoftView.onSignOut()
        }
    }

    override fun microsoftLogin(activity: Activity) {
        microsoftView.waitResult()
        cannotExit = true
        val oAuthProvider = OAuthProvider.newBuilder("microsoft.com")
       // oAuthProvider.addCustomParameter("prompt", "consent")
        oAuthProvider.addCustomParameter("login_hint", "somemail@hotmail.com.br")
        scopes = ArrayList()
        scopes.add("mail.read")
        oAuthProvider.scopes = scopes
           firebaseInstance().startActivityForSignInWithProvider(activity, oAuthProvider.build())
                .addOnSuccessListener {authResult->
                    val str = authResult.additionalUserInfo?.profile
                    val json = JSONObject(str!!)
                    val name = json.getString("givenName")

                    Log.d("microsoftSuccess tk", "profile: $str")

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        microsoftView.handleSSLHandshake()
                        stringResponse(activity, (authResult.credential as OAuthCredential).accessToken)
                    }else{
                        stringResponse(activity, (authResult.credential as OAuthCredential).accessToken)
                    }

                    cannotExit = false
                    preferences.setStringSP(activity, "microsoftName", name)

                }
                   .addOnFailureListener {
                        cannotExit = if(it.message ==  activity.getString(R.string.operation_was_canceled)){
                            microsoftView.onFail(it.message!!)
                            false
                        }else{
                            val error = String.format(
                                    activity.getString(R.string.accountAlreadyExists)+": %s ",
                                    activity.getString(R.string.findProvider))
                            microsoftView.onFail(error)
                            microsoftView.findProvider()
                            false
                        }
                    }
    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            microsoftView.onSignOut()
        }
    }

    private fun stringResponse(context: Context, accessToken: String){
        val queue = Volley.newRequestQueue(context)
        val stringObjectRequest: StringRequest = object : StringRequest(Method.GET,
                "https://graph.microsoft.com/beta/me/photo/$"+"value",
                    Response.Listener {
                               val inputStr = getInputStream(it, "iso8859-1")

                                saveImage(context, inputStr)
                            }, Response.ErrorListener { error ->
                                if (error?.message != null) {
                                    Log.e("microsoftSuccess error", error.message!!)
                       }
                    }
                ){
                override fun getParams(): Map<String, String> {
                    val params: Map<String, String>
                    params = hashMapOf("key" to "value")
                    return params
                }

                override fun getHeaders(): Map<String, String> {
                    val headersV: Map<String, String>
                    headersV = hashMapOf(
                            "Authorization" to "Bearer $accessToken"
                    )
                    return headersV
                }
         }
            stringObjectRequest.retryPolicy = DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        queue.add(stringObjectRequest)
    }

    fun getInputStream(str: String, encoding: String?): InputStream {
        return ByteArrayInputStream(str.toByteArray(charset(encoding!!)))
    }

     private lateinit var currentPhotoPath: String
    fun saveImage(context: Context, inputStr: InputStream){
        val dis = DataInputStream(inputStr)
        val buffer = ByteArray(1024)
        var length: Int

        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val fos = FileOutputStream(
                File(storageDir, "microsoft.png").apply {
                    currentPhotoPath = absolutePath
                }.also {
                    it.also {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            val permissionTemp = Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            context.grantUriPermission(
                                    context.packageName,
                                    Uri.fromFile(it),
                                    permissionTemp
                            )
                            val uriPermission = Uri.fromFile(it)
                            preferences.setStringSP(context, "microsoftImage", "$uriPermission")
                        } else {
                            preferences.setStringSP(context, "microsoftImage", "$it")
                        }
                    }
                }
        )

        while (dis.read(buffer).also { length = it } > 0) {
            fos.write(buffer, 0, length)
        }
        microsoftView.onSuccess(
                preferences.getStringSP(context, "microsoftName"),
                preferences.getStringSP(context, "microsoftImage")
        )
    }

    override fun findProvider(activity: Activity, email: String) {
        microsoftView.waitResult()
        cannotExit = true
        if(TextUtils.isEmpty(email)){
            microsoftView.onFail(activity.getString(R.string.fill_field)+" | "+activity.getString(R.string.findProvider))
            cannotExit = false
        }else{
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{ it ->
                it.addOnSuccessListener {
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${it.signInMethods}")
                    microsoftView.onFail(error)
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