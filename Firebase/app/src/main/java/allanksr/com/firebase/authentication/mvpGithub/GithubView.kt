package allanksr.com.firebase.authentication.mvpGithub

interface GithubView {
    fun onFail(fail: String)
    fun onSuccess(name: String, image: String)
    fun onSignOut()
    fun waitResult()
    fun findProvider()
}