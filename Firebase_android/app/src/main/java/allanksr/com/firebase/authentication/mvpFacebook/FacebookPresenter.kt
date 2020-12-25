package allanksr.com.firebase.authentication.mvpFacebook

import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth

interface FacebookPresenter {
    fun firebaseInstance(): FirebaseAuth
    fun signOut()
    fun firebaseAuth()
    fun loginManager(fragment: Fragment, callbackManager : CallbackManager)
    fun exitFragment()
}