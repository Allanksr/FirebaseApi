package allanksr.com.firebase.authentication.mvpFacebook

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo

class FacebookImp(
        private var facebookView: FacebookView,
        private var preventExit: PreventExit
): FacebookPresenter {
    private var cannotExit = false

    override fun firebaseInstance(): FirebaseAuth {
       return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth() {
        if(firebaseInstance().currentUser !=null){
            for (profile : UserInfo in firebaseInstance().currentUser?.providerData!!){
                if(profile.providerId.contains("facebook.com")){
                    Log.d("providerId", profile.providerId)
                    val user = firebaseInstance().currentUser
                    val faceId = profile.uid
                    val photoUrl = "https://graph.facebook.com/$faceId/picture?type=large"
                    facebookView.onSignIn(user?.displayName!!, user.email!!, photoUrl)
                }
            }
        }else{
            facebookView.onSignOut()
        }

    }

        override fun loginManager(fragment: Fragment, callbackManager: CallbackManager) {

            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            facebookView.waitResult()
                            cannotExit = true
                            val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                            val request = GraphRequest.newMeRequest(loginResult.accessToken) { json, _ ->
                                firebaseInstance().fetchSignInMethodsForEmail(json.getString("email")).continueWith {
                                    if (it.result.signInMethods!!.isEmpty()) {

                                        firebaseInstance().signInWithCredential(credential).addOnCompleteListener { task ->
                                            cannotExit = if (!task.isSuccessful) {
                                                facebookView.onFail("${task.exception}")
                                                false
                                            } else {
                                                for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                                                    val user = firebaseInstance().currentUser
                                                    val faceId = profile.uid
                                                    val photoUrl = "https://graph.facebook.com/$faceId/picture?type=large"
                                                    facebookView.onSignIn(user?.displayName!!, user.email!!, photoUrl)
                                                }
                                                false
                                            }
                                        }
                                    } else {
                                        if (it.result.signInMethods.toString() == "[facebook.com]") {
                                            firebaseInstance().signInWithCredential(credential).addOnCompleteListener { task ->
                                                cannotExit = if (!task.isSuccessful) {
                                                    facebookView.onFail("${task.exception}")
                                                    false
                                                } else {
                                                    for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                                                        val user = firebaseInstance().currentUser
                                                        val faceId = profile.uid
                                                        val photoUrl = "https://graph.facebook.com/$faceId/picture?type=large"
                                                        facebookView.onSignIn(user?.displayName!!, user.email!!, photoUrl)
                                                    }
                                                    false
                                                }
                                            }
                                        } else {
                                            val error = String.format(fragment.getString(R.string.accountAlreadyExists) + ": %s ", "${it.result.signInMethods}")
                                            facebookView.onFail(error)
                                            cannotExit = false
                                        }
                                    }
                                }
                            }
                            val parameters = Bundle()
                            parameters.putString("fields", "email")
                            request.parameters = parameters
                            request.executeAsync()
                        }

                        override fun onCancel() {
                            facebookView.onFail("onCancel")
                            cannotExit = false
                        }

                        override fun onError(exception: FacebookException) {
                            facebookView.onFail("${exception.message}")
                            cannotExit = false
                        }
                    })
        }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            facebookView.onSignOut()
        }
    }

    override fun exitFragment() {
        if(cannotExit){
            preventExit.waitToExit()
        }else{
            preventExit.exitActivity()
        }
    }

}