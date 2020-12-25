package allanksr.com.firebase.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


object SomeAlarmManager {
    private var pendingIntent: PendingIntent? = null

    fun setAlarm(context: Context, alarmTime: Long, message: String) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).setAction(AlarmReceiver.ACTION_SEND_TEST_MESSAGE)
        intent.putExtra(AlarmReceiver.EXTRA_MESSAGE, message)

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + alarmTime, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        pendingIntent?.let {
            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
        }
    }

}