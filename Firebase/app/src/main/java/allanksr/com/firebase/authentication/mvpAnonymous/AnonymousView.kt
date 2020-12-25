package allanksr.com.firebase.authentication.mvpAnonymous

interface AnonymousView {
    fun onSuccess(name: String)
    fun onFail(fail: String)
    fun signOut()
    fun waitResult()
}