package allanksr.com.firebase.authentication.mvpGithub

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface GithubPresenter {
    fun firebaseAuth(activity: Activity)
    fun firebaseInstance(): FirebaseAuth
    fun githubLogin(activity: Activity)
    fun signOut()
    fun findProvider(activity: Activity, email: String)
    fun exitActivity()
}