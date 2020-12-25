package allanksr.com.firebase.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == ACTION_SEND_TEST_MESSAGE) {
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            println(message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val ACTION_SEND_TEST_MESSAGE = "ACTION_SEND_TEST_MESSAGE"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }

}