package allanksr.com.firebase.authentication.mvpMicrosoft

interface MicrosoftView {
    fun onFail(fail: String)
    fun onSuccess(name: String, image: String)
    fun onSignOut()
    fun waitResult()
    fun handleSSLHandshake()
    fun findProvider()
}