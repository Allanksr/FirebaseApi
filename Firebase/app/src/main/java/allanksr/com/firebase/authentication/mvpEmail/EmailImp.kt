package allanksr.com.firebase.authentication.mvpEmail

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import allanksr.com.firebase.constantes
import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo

class EmailImp(
        private var mailView: MailView,
        private var preventExit: PreventExit) :
        EmailPresenter {
    private var cannotExit = false
    override fun createAccountWithEmail(activity: Activity, email: String, password: String) {
        if(TextUtils.isEmpty((email)) || TextUtils.isEmpty(password)){
            mailView.firebaseCreateUserWithEmailFail(activity.getString(R.string.fill_field))
        }else{
            cannotExit = true
            mailView.waitResult()
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{
                if(it.result.signInMethods!!.isEmpty()){
                      firebaseInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->
                            cannotExit = if (task.isSuccessful) {
                                val user = firebaseInstance().currentUser
                                mailView.firebaseCreateUserWithEmailSuccess("${user?.email}")
                                false
                            } else {
                                val error = String.format(activity.getString(R.string.error)+": %s ", "${task.exception}")
                                mailView.firebaseCreateUserWithEmailFail(error)
                                false
                            }
                        }
                }else{
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${it.result.signInMethods}")
                    mailView.firebaseSiginWithEmailFail(error)
                    cannotExit = false
                }
            }

        }
    }
    override fun siginWithEmail(activity: Activity, email: String, password: String) {
        if(TextUtils.isEmpty((email)) || TextUtils.isEmpty(password)){
            mailView.firebaseSiginWithEmailFail(activity.getString(R.string.fill_field))
        }else{
            cannotExit = true
            mailView.waitResult()
            firebaseInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        cannotExit = if (task.isSuccessful) {
                            val user = firebaseInstance().currentUser
                            mailView.firebaseSiginWithEmailSuccess("${user?.email}")
                            false
                        }else{
                            val error = String.format(activity.getString(R.string.error)+": %s ", "${task.exception}")
                            mailView.firebaseSiginWithEmailFail(error)
                            if(task.exception?.message.equals(constantes.errorPass)){
                                mailView.resetPassword()
                            }
                            false
                        }
                    }
        }
    }

    override fun siginWithWithoutPassword(activity: Activity, email: String) {
        if(TextUtils.isEmpty(email)){
            mailView.firebaseSiginWithEmailFail(activity.getString(R.string.fill_field))
        }else{
            cannotExit = true
            mailView.waitResult()
            firebaseInstance().fetchSignInMethodsForEmail(email).continueWith{
                if(it.result.signInMethods!!.isEmpty()){

                    val url = "https://allanksr.page.link/app?auth=$email"
                    val actionCodeSettings = ActionCodeSettings.newBuilder()
                            .setUrl(url)
                            .setHandleCodeInApp(true)
                            .setAndroidPackageName("allanksr.com.firebase", true, null)
                            .build()

                    firebaseInstance().sendSignInLinkToEmail(email, actionCodeSettings)
                            .addOnCompleteListener { task ->
                                cannotExit = if (task.isSuccessful) {
                                    val mailTo = String.format(activity.getString(R.string.signin_link_was_sent)+": %s ", email)
                                    mailView.checkEmail(mailTo, email)
                                    false
                                }else{
                                    val error = String.format(activity.getString(R.string.error)+": %s", "${task.exception}")
                                    mailView.firebaseSiginWithEmailFail(error)
                                    false
                                }
                            }

                }else{
                    val error = String.format(activity.getString(R.string.accountAlreadyExists)+": %s ", "${it.result.signInMethods}")
                    mailView.firebaseSiginWithEmailFail(error)
                    cannotExit = false
                }
            }
        }
    }

    override fun firebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    override fun firebaseAuth() {
        if(firebaseInstance().currentUser !=null){
            for (profile : UserInfo in firebaseInstance().currentUser?.providerData!!){
                if(profile.providerId.contains("password")){
                Log.d("providerId", profile.providerId)
                mailView.firebaseUser(firebaseInstance().currentUser?.email!!)
               }
            }
        }else{
            mailView.viewSignOut()
        }
    }
    override fun firebaseSignOut() {
       firebaseInstance().signOut()
        if(firebaseInstance().currentUser==null){
            mailView.viewSignOut()
        }
    }

    override fun exitActivity() {
        if(cannotExit){
            preventExit.waitToExit()
        }else{
            preventExit.exitActivity()
        }
    }

    override fun resetPasswordLink(activity: Activity, email: String) {
        if(TextUtils.isEmpty(email)){
            mailView.firebaseCreateUserWithEmailFail(activity.getString(R.string.fill_field))
        }else{
            cannotExit = true
            mailView.waitResult()
            firebaseInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    val mailTo = String.format(activity.getString(R.string.reset_link_was_sent)+": %s ", email)
                    mailView.checkEmail(mailTo, email)
                    cannotExit = false
                }else{
                    mailView.firebaseSiginWithEmailFail(it.exception?.message!!)
                }
            }
        }
    }


}
























