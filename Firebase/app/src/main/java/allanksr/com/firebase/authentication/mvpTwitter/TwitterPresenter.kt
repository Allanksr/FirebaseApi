package allanksr.com.firebase.authentication.mvpTwitter

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface TwitterPresenter {
    fun firebaseAuth(activity: Activity)
    fun firebaseInstance(): FirebaseAuth
    fun twitterLogin(activity: Activity)
    fun findProvider(activity: Activity, email: String)
    fun signOut()
    fun exitActivity()
}