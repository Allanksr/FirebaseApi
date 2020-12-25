package allanksr.com.firebase.authentication.mvpSmartphone

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

interface PhonePresenter {
    fun firebaseInstance(): FirebaseAuth
    fun phoneAuthProvider(): PhoneAuthProvider
    fun verifyPhoneNumber(number: String, activity: Activity)
    fun receivedCode(activity: Activity, verificationCode: String)
    fun onCodeReceive(activity: Activity, verificationCode: String, storedVerificationId: String)
    fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential)
    fun firebaseAuth(activity: Activity)
    fun signOut()
    fun exitActivity()
}