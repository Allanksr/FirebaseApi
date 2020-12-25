package allanksr.com.firebase.authentication.mvpGoogle

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

interface GooglePresenter {
    fun firebaseInstance(): FirebaseAuth
    fun signIn(activity: Activity, googleIntent: Intent)
    fun firebaseSignOut()
    fun firebaseAuth()
    fun exitActivity()
}