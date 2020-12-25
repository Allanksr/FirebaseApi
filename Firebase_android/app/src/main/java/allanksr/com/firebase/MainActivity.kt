package allanksr.com.firebase


import allanksr.com.firebase.admob.AdmobActivity
import allanksr.com.firebase.authentication.AuthenticationActivity
import allanksr.com.firebase.authentication.mvpEmail.Email
import allanksr.com.firebase.cloudFirestore.FirestoreActivity
import allanksr.com.firebase.crashlytics.CrashlyticsActivity
import allanksr.com.firebase.cloudFunctions.FunctionsActivity
import allanksr.com.firebase.cloudMessage.CloudMessageActivity
import allanksr.com.firebase.hosting.HostingActivity
import allanksr.com.firebase.realtimeDatabase.DatabaseActivity
import allanksr.com.firebase.cloudStorage.StorageActivity
import allanksr.com.firebase.remoteConfig.RemoteConfigActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activities.*
import kotlinx.android.synthetic.main.activities_choice.view.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: AppCompatActivity() {
    private lateinit var activitiesName: Array<String>
    private lateinit var activitiesIntent: Array<Intent>
    val auth = FirebaseAuth.getInstance()
    private var preferences = Prefs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val intent = intent
        val emailLink = intent.data.toString()
        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            val email = preferences.getStringSP(this, "email")
            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Successfully signed", "Successfully signed in with email link!")
                            startActivity(Intent(this, Email::class.java))
                            // val result = task.result
                            // You can access the new user via result.getUser()
                            // Additional user info profile *not* available via:
                            // result.getAdditionalUserInfo().getProfile() == null
                            // You can check if the user is new or existing:
                            // result.getAdditionalUserInfo().isNewUser()
                        } else {
                            Log.e("Error signing", "Error signing in with email link", task.exception)
                            tv_error.text = "${task.exception}"
                        }
                    }
        }

        activitiesName = arrayOf(
            getString(R.string.firebaseAuthentication),
            getString(R.string.firestoreCloudStore),
            getString(R.string.databaseActivity),
            getString(R.string.storageActivity),
            getString(R.string.hostingActivity),
            getString(R.string.functionsActivity),
            getString(R.string.crashlyticsActivity),
            getString(R.string.messageActivity),
            getString(R.string.remoteConfigActivity),
            getString(R.string.admobActivity)
        )
        activitiesIntent = arrayOf(
            Intent(this, AuthenticationActivity::class.java),
            Intent(this, FirestoreActivity::class.java),
            Intent(this, DatabaseActivity::class.java),
            Intent(this, StorageActivity::class.java),
            Intent(this, HostingActivity::class.java),
            Intent(this, FunctionsActivity::class.java),
            Intent(this, CrashlyticsActivity::class.java),
            Intent(this, CloudMessageActivity::class.java),
            Intent(this, RemoteConfigActivity::class.java),
            Intent(this, AdmobActivity::class.java)
        )

        for(a in activitiesName.indices){
            val choiceStoreLL = LayoutInflater
                    .from(applicationContext)
                    .cloneInContext(ContextThemeWrapper(this, R.style.AppTheme))
                    .inflate(R.layout.activities_choice, null) as RelativeLayout
            llContainer_activities.addView(choiceStoreLL)

            choiceStoreLL.btn_choice.text = activitiesName[a]
            choiceStoreLL.btn_choice.setOnClickListener{
                activitiesIntent[a].flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(activitiesIntent[a])
            }
        }
    }
}