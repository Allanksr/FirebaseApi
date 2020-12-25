package allanksr.com.firebase.authentication.mvpAnonymous

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo


class AnonymousImp (private var anonymousView: AnonymousView): AnonymousPresenter {
    override fun firebaseAuth(activity: Activity) {
        if (firebaseInstance().currentUser != null) {
            for (profile: UserInfo in firebaseInstance().currentUser?.providerData!!) {
                    if(profile.providerId.contains("firebase")){
                    Log.d("providerId", "Anonymous ${profile.providerId}")
                    anonymousView.onSuccess("signInAnonymously")
                }else{
                    anonymousView.signOut()
                }
            }
        }else{
            anonymousView.signOut()
        }

    }

    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun anonymousLogin(activity: Activity) {
        anonymousView.waitResult()
        firebaseInstance().signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseInstance().currentUser?.displayName
                        Log.d("Anonymous", "signInAnonymously:success $user")
                        anonymousView.onSuccess("signInAnonymously")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Anonymous", "signInAnonymously:failure", task.exception)
                        anonymousView.onFail("${task.exception}")
                    }

                }


    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            anonymousView.signOut()
        }
    }
}