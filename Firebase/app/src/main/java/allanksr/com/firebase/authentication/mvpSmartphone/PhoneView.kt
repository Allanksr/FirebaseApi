package allanksr.com.firebase.authentication.mvpSmartphone


interface PhoneView {
    fun signInWithCredentialSuccess(success: String)
    fun signInWithCredentialFail(fail: String)
    fun firebaseUser(phoneNumber: String)
    fun onVerificationFailed(error: String)
    fun confirmCode()
    fun firebaseAuth()
    fun firebaseSignOut()
    fun waitResult()
    fun isVerifyingPhoneNumber()
}