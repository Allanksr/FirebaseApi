package allanksr.com.firebase.cloudMessage

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.cloud_message_activity.*
import kotlinx.android.synthetic.main.loading.*


class CloudMessageActivity: AppCompatActivity(), CloudView {



    private var prefs = Prefs()
    private lateinit var cloudPresenter: CloudPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cloud_message_activity)

        cloudPresenter = CloudImp(this)

        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("CloudMessaging", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            if (prefs.getStringSP(this, "onNewToken").isEmpty()) {
                val token = task.result
                prefs.setStringSP(this, "onNewToken", token)
                Log.d("CloudMessaging", ": token saved once")
            }
        })

        //Uncommon for sending messages in the background to all devices
        //FirebaseMessaging.getInstance().subscribeToTopic("firebasetest")

        bt_send_message.setOnClickListener{
            cloudPresenter.sendNotification(this, arrayListOf("1", edt_message.text.toString()))
            edt_message.setText("")
        }

        bt_send_message_minimize.setOnClickListener{
            moveTaskToBack(true)
            cloudPresenter.sendNotification(this, arrayListOf("1", edt_message.text.toString()))
            edt_message.setText("")
        }

        bt_send_background_message.setOnClickListener{
            moveTaskToBack(true)
            cloudPresenter.sendBackgroundData(this, arrayListOf("0", edt_message.text.toString()))
            edt_message.setText("")
        }

    }

    override fun waitResult() {
        tv_response.visibility = View.GONE
        tv_response.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

    override fun singleResult(result: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_response.visibility = View.VISIBLE
        tv_response.text = result
    }
}
