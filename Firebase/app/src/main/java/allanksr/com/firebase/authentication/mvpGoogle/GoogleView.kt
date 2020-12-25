package allanksr.com.firebase.authentication.mvpGoogle

interface GoogleView {
    fun onSignIn(name: String, email: String, photo: String)
    fun onSignOut()
    fun onFail(fail: String)
    fun waitResult()
}