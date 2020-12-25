package allanksr.com.firebase.authentication.mvpPlayGames

interface PlayGamesView {
    fun onSignIn(name: String, photo: String)
    fun onSignOut()
    fun onFail(fail: String)
    fun waitResult()
}