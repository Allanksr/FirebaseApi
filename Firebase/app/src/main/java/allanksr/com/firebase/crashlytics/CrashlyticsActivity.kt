package allanksr.com.firebase.crashlytics

import allanksr.com.firebase.R
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.crashlytics_activity.*


class CrashlyticsActivity: AppCompatActivity(){
    lateinit var firebaseCrashlytics: FirebaseCrashlytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crashlytics_activity)

        firebaseCrashlytics =  FirebaseCrashlytics.getInstance()
        bt_crash.setOnClickListener{
            val exception = Exception("Crashlytics Exception Test")
            firebaseCrashlytics.recordException(exception)
            Toast.makeText(this, "Crashlytics Exception Test", Toast.LENGTH_SHORT).show()
        }

        bt_throw_exception.setOnClickListener{
            throw NullPointerException()
        }

    }
}
