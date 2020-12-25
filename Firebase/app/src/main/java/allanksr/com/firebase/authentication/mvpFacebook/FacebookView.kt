package allanksr.com.firebase.authentication.mvpFacebook

interface FacebookView {
    fun onSignIn(name: String, email: String, photo: String)
    fun onSignOut()
    fun onFail(fail: String)
    fun waitResult()
}