package allanksr.com.firebase.authentication.mvpPlayGames

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PlayGamesAuthProvider
import com.google.firebase.auth.UserInfo

class PlayGamesImp (
        private var playGamesView: PlayGamesView,
        private var preventExit: PreventExit):
        PlayGamesPresenter{
    private var cannotExit = false
    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseAuth() {
        if(firebaseInstance().currentUser !=null){
            for (profile : UserInfo in firebaseInstance().currentUser?.providerData!!){
                if(profile.providerId.contains("playgames")){
                    Log.d("providerId", profile.providerId)
                    val user = firebaseInstance().currentUser
                    playGamesView.onSignIn(user?.displayName!!, "${user.photoUrl}")
                }
            }
        }else{
            playGamesView.onSignOut()
        }
    }

    override fun signIn(activity: Activity, activityResult: ActivityResult) {
        playGamesView.waitResult()
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val intent = activityResult.data
            val dataResult = Auth.GoogleSignInApi.getSignInResultFromIntent(intent)

            firebaseInstance().fetchSignInMethodsForEmail("${dataResult?.signInAccount?.email}").continueWith{
                if(it.result.signInMethods!!.isEmpty()){
                    if (dataResult!!.isSuccess) {
                        firebaseAuthWithPlayGames(dataResult.signInAccount!!.serverAuthCode!!)
                    } else {
                        playGamesView.onFail("$activityResult")
                        cannotExit = false
                    }
                }else{
                    val error = String.format(activity.getString(R.string.error)+": %s ", "${it.exception}")
                    if(it.exception!=null){
                        playGamesView.onFail(error)
                    }else{
                        firebaseAuthWithPlayGames(dataResult?.signInAccount!!.serverAuthCode!!)
                    }
                    cannotExit = false
                }
            }
        }else {
            playGamesView.onFail("$activityResult")
        }
    }

    private fun firebaseAuthWithPlayGames(serverAuthCode :String){
        val credential = PlayGamesAuthProvider.getCredential(serverAuthCode)
        firebaseInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                cannotExit = if (task.isSuccessful) {
                    val user = firebaseInstance().currentUser
                    playGamesView.onSignIn(user?.displayName!!, "${user.photoUrl}")
                    false
                } else {
                    playGamesView.onFail("${task.exception}")
                    false
                }
            }
    }

    override fun signOut() {
        firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            playGamesView.onSignOut()
        }
    }
    override fun exitActivity() {
        if(cannotExit){
            preventExit.waitToExit()
        }else{
            preventExit.exitActivity()
        }
    }

}