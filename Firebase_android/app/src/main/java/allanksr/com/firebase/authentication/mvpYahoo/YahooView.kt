package allanksr.com.firebase.authentication.mvpYahoo

interface YahooView {
    fun onFail(fail: String)
    fun onSuccess(name: String, image: String)
    fun onSignOut()
    fun waitResult()
    fun findProvider()
}