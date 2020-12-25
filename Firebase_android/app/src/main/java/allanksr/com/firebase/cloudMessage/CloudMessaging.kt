package  allanksr.com.firebase.cloudMessage


import allanksr.com.firebase.Prefs
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class CloudMessaging:  FirebaseMessagingService() {
    private var prefs = Prefs()
    override fun onNewToken(token: String) {
        if(prefs.getStringSP(this, "onNewToken").isEmpty()){
            prefs.setStringSP(this, "onNewToken", token)
            Log.d("CloudMessaging", ": token saved once")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.data["checkUpdate"] == "0"){
            prefs.setStringSP(this, "checkUpdate", "${remoteMessage.data["checkUpdate"]}")
            Log.d("CloudMessaging", ": ${remoteMessage.data["checkUpdate"]}")
            Log.d("CloudMessaging", ": ${remoteMessage.data["backGroundMessage"]}")

            val mDisplayAlert = Intent(this, DisplayAlert::class.java)
            mDisplayAlert.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mDisplayAlert.putExtra("title", "backGroundMessage")
            mDisplayAlert.putExtra("body", "${remoteMessage.data["backGroundMessage"]}")
            startActivity(mDisplayAlert)
        }else{
            val mDisplayAlert = Intent(this, DisplayAlert::class.java)
            mDisplayAlert.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mDisplayAlert.putExtra("title", remoteMessage.notification?.title)
            mDisplayAlert.putExtra("body", remoteMessage.notification?.body)
            startActivity(mDisplayAlert)
        }
    }


}