package allanksr.com.firebase.authentication.mvpSmartphone

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserInfo
import java.util.*
import java.util.concurrent.TimeUnit
import java.lang.Integer.parseInt

class PhoneImp(
        var phoneView: PhoneView,
        private var preventExit: PreventExit):
        PhonePresenter {
    var cannotExit = false
    private var preferences = Prefs()
    lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private var timeNowLessTimeSaved = 0
    var expired = 0
    lateinit var context: Activity
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    override fun phoneAuthProvider(): PhoneAuthProvider {
        return PhoneAuthProvider.getInstance()
    }
    override fun verifyPhoneNumber(number: String, activity: Activity) {
        context = activity
        if(TextUtils.isEmpty(number)){
            phoneView.onVerificationFailed(activity.getString(R.string.fill_field))
        }else{
            phoneView.waitResult()
            cannotExit = true
           phoneAuthProvider().verifyPhoneNumber(
                    number, // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    activity, // Activity (for callback binding)
                    callbacks // OnVerificationStateChangedCallbacks
            )
            //SomeAlarmManager.setAlarm(activity, 15000, "Test Message!")

        }
    }
    override fun receivedCode(activity: Activity, verificationCode: String) {
        if(TextUtils.isEmpty(verificationCode)){
            phoneView.onVerificationFailed(activity.getString(R.string.fill_field))
        }else{
            val storedVerificationId= preferences.getStringSP(activity,"storedVerificationId")
            onCodeReceive(activity, verificationCode, storedVerificationId)
            phoneView.waitResult()
        }
    }
    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(context, credential)
        }

       override fun onVerificationFailed(e: FirebaseException) {
           phoneView.signInWithCredentialFail("$e")
        }
        override fun onCodeSent(verificationId: String, resending: PhoneAuthProvider.ForceResendingToken) {
            // Save verification ID and resending token so we can use them later
            resendToken = resending
            phoneView.confirmCode()
            cannotExit = true
            preferences.setStringSP(context,"storedVerificationId", verificationId)
            val timeNow = (Date().time / 1000).toString()
            //phoneAuthProvider() Timeout duration
            val timeNowPlusExpireTime = parseInt(timeNow, 10) + 60
            expired = preferences.getIntSP(context,"timeSaved")
            if(expired==0){
                preferences.setIntSP(context,"timeSaved", timeNowPlusExpireTime)
            }
        }

    }


    override fun onCodeReceive(activity: Activity, verificationCode: String, storedVerificationId: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, verificationCode)
        signInWithPhoneAuthCredential(activity, credential)
    }
    override fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential) {
        firebaseInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    cannotExit = if (task.isSuccessful) {
                        val user = task.result?.user
                        phoneView.signInWithCredentialSuccess("${user?.phoneNumber}")
                        preferences.setIntSP(activity,"timeSaved", 0)
                        false
                    } else {
                        phoneView.signInWithCredentialFail("${task.exception}")
                        false
                    }
                }
    }

    override fun firebaseAuth(activity: Activity) {
        if(firebaseInstance().currentUser !=null){
            for (profile : UserInfo in firebaseInstance().currentUser?.providerData!!){
                if(profile.providerId.contains("phone")){
                    Log.d("providerId", profile.providerId)
                    phoneView.firebaseUser(firebaseInstance().currentUser?.phoneNumber!!)
                }
            }
        }else{
            expired = preferences.getIntSP(activity,"timeSaved")
            if(expired>0){
                val timeNowCalculate = (Date().time / 1000).toString()
                //phoneAuthProvider() calculate Timeout duration
                val timeNowPlusExpireTime = parseInt(timeNowCalculate, 10) + 60
                timeNowLessTimeSaved = timeNowPlusExpireTime-expired
                cannotExit = if (timeNowLessTimeSaved < 60) {
                    Log.d("currentTimeMillis:"," valid ${timeNowLessTimeSaved}/60")
                    phoneView.confirmCode()
                    false
                }else{
                    Log.d("currentTimeMillis:","expired ${timeNowLessTimeSaved}/60")
                    preferences.setIntSP(activity,"timeSaved", 0)
                    phoneView.firebaseAuth()
                    false
                }
            }else{
                phoneView.firebaseAuth()
                cannotExit = false
            }
        }
    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            phoneView.firebaseSignOut()
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