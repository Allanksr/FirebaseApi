package allanksr.com.firebase.authentication.mvpTwitter

interface TwitterView {
    fun onFail(fail: String)
    fun onSuccess(name: String, image: String)
    fun onSignOut()
    fun waitResult()
    fun findProvider()
}