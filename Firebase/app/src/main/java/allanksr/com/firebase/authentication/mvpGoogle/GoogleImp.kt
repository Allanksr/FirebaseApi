package allanksr.com.firebase.authentication.mvpGoogle

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserInfo


class GoogleImp(
        private var googleView: GoogleView,
        private var preventExit: PreventExit):
        GooglePresenter {
    private var cannotExit = false
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth() {
        if(firebaseInstance().currentUser !=null){
            for (profile : UserInfo in firebaseInstance().currentUser?.providerData!!){
                Log.d("providerId", profile.providerId)
                 if(profile.providerId.contains("google.com")){
                    val user = firebaseInstance().currentUser
                    googleView.onSignIn(user?.displayName!!, user.email!!, "${user.photoUrl}")
                 }
            }
        }else{
            googleView.onSignOut()
        }
    }

    override fun signIn(activity: Activity, googleIntent: Intent) {
        googleView.waitResult()
        cannotExit = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(googleIntent)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseInstance().fetchSignInMethodsForEmail("${account.email}").continueWith{
                if(it.result.signInMethods!!.isEmpty()){
                    firebaseAuthWithGoogle("${account.idToken}")
                    cannotExit = false
                }else{
                    val error = String.format(activity.getString(R.string.error)+": %s ", "${it.exception}")
                    if(it.exception!=null){
                        googleView.onFail(error)
                    }else{
                        firebaseAuthWithGoogle("${account.idToken}")
                    }
                }
            }
        } catch (e: ApiException) {
            googleView.onFail("$e")
            cannotExit = false
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseInstance().currentUser
                        googleView.onSignIn(user?.displayName!!, user.email!!, "${user.photoUrl}")
                    } else {
                        googleView.onFail("${task.exception}")
                    }
                    cannotExit = false
                }
    }

    override fun firebaseSignOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            googleView.onSignOut()
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


