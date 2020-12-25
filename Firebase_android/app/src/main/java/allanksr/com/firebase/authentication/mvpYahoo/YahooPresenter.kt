package allanksr.com.firebase.authentication.mvpYahoo

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface YahooPresenter {
    fun firebaseAuth(activity: Activity)
    fun firebaseInstance(): FirebaseAuth
    fun yahooLogin(activity: Activity)
    fun signOut()
    fun findProvider(activity: Activity, email: String)
    fun exitActivity()
}