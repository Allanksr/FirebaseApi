package allanksr.com.firebase.authentication.mvpAnonymous

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface AnonymousPresenter {
    fun firebaseAuth(activity: Activity)
    fun firebaseInstance(): FirebaseAuth
    fun anonymousLogin(activity: Activity)
    fun signOut()
}