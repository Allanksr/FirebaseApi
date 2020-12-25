package allanksr.com.firebase.authentication.mvpEmail

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface EmailPresenter {
    fun createAccountWithEmail(activity: Activity, email: String, password: String)
    fun siginWithEmail(activity: Activity, email: String, password: String)
    fun siginWithWithoutPassword(activity: Activity, email: String)
    fun firebaseInstance(): FirebaseAuth
    fun firebaseAuth()
    fun firebaseSignOut()
    fun exitActivity()
    fun resetPasswordLink(activity: Activity, email: String)
}