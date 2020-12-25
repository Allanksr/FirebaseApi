package allanksr.com.firebase.authentication.mvpEmail

interface MailView {
    fun firebaseCreateUserWithEmailSuccess(success: String)
    fun firebaseCreateUserWithEmailFail(fail: String)
    fun firebaseSiginWithEmailSuccess(success: String)
    fun firebaseSiginWithEmailFail(fail: String)
    fun firebaseUser(userEmail: String)
    fun checkEmail(checkEmail: String, email: String)
    fun viewSignOut()
    fun waitResult()
    fun resetPassword()
}