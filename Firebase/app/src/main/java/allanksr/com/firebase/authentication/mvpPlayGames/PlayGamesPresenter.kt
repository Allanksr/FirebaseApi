package allanksr.com.firebase.authentication.mvpPlayGames

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.FirebaseAuth

interface PlayGamesPresenter {
    fun firebaseInstance(): FirebaseAuth
    fun signIn(activity: Activity, activityResult: ActivityResult)
    fun signOut()
    fun firebaseAuth()
    fun exitActivity()
}