package allanksr.com.firebase.authentication

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.mvpAnonymous.Anonymous
import allanksr.com.firebase.authentication.mvpEmail.Email
import allanksr.com.firebase.authentication.mvpFacebook.Facebook
import allanksr.com.firebase.authentication.mvpGithub.Github
import allanksr.com.firebase.authentication.mvpGoogle.Google
import allanksr.com.firebase.authentication.mvpMicrosoft.Microsoft
import allanksr.com.firebase.authentication.mvpPlayGames.PlayGames
import allanksr.com.firebase.authentication.mvpSmartphone.Phone
import allanksr.com.firebase.authentication.mvpTwitter.Twitter
import allanksr.com.firebase.authentication.mvpYahoo.Yahoo
import allanksr.com.firebase.constantes.imageXml
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.activities.*
import kotlinx.android.synthetic.main.methods_choice.view.*

class AuthenticationActivity: AppCompatActivity() {

    private lateinit var activitiesName: Array<String>
    private lateinit var activitiesIntent: Array<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_activity)


        activitiesName = arrayOf(
            getString(R.string.firebaseEmailAuthentication),
            getString(R.string.firebasePhoneAuthentication),
            getString(R.string.firebaseGoogleAuthentication),
            getString(R.string.firebasePlayGamesAuthentication),
            getString(R.string.firebaseFacebookAuthentication),
            getString(R.string.firebaseTwitterAuthentication),
            getString(R.string.firebaseGithubAuthentication),
            getString(R.string.firebaseYahooAuthentication),
            getString(R.string.firebaseMicrosoftAuthentication),
            getString(R.string.firebaseAnonymousAuthentication)
        )
        activitiesIntent = arrayOf(
            Intent(this, Email::class.java),
            Intent(this, Phone::class.java),
            Intent(this, Google::class.java),
            Intent(this, PlayGames::class.java),
            Intent(this, Facebook::class.java),
            Intent(this, Twitter::class.java),
            Intent(this, Github::class.java),
            Intent(this, Yahoo::class.java),
            Intent(this, Microsoft::class.java),
            Intent(this, Anonymous::class.java)
        )

        for(a in activitiesName.indices){
            val drawable = AppCompatResources.getDrawable(this, imageXml[a])
            val choiceStoreLL = LayoutInflater
                    .from(applicationContext)
                    .cloneInContext(ContextThemeWrapper(this, R.style.AppTheme))
                    .inflate(R.layout.methods_choice, null) as RelativeLayout
            llContainer_activities.addView(choiceStoreLL)

            choiceStoreLL.btn_choice.text = activitiesName[a]
            choiceStoreLL.iv_anonymous.setImageDrawable(drawable)
            choiceStoreLL.btn_choice.setOnClickListener{
                activitiesIntent[a].flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(activitiesIntent[a])
            }
        }
    }
}