package allanksr.com.firebase.authentication.mvpMicrosoft

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface MicrosoftPresenter {
    fun firebaseAuth(activity: Activity)
    fun firebaseInstance(): FirebaseAuth
    fun microsoftLogin(activity: Activity)
    fun signOut()
    fun findProvider(activity: Activity, email: String)
    fun exitActivity()
}