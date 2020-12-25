package allanksr.com.firebase.remoteConfig

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.remote_config_activity.*
import java.util.*


class RemoteConfigActivity: AppCompatActivity(){
    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
   // var cacheExpiration: Long = 3600 // 1 hour in seconds.
    var cacheExpirationMinute: Long = 59 // 1 minutes

    private var preferences = Prefs()
    private var timeNowLessTimeSaved = 0
    var expired = 0

    private var runnableCode: Runnable? = null
    private var handlerLooper = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remote_config_activity)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.fetchAndActivate()

        cacheRemote()

        bt_request_remoteData.setOnClickListener{
            val remoteParam = mFirebaseRemoteConfig.getString("data")
            tv_remoteConfig_response.text = remoteParam
            Log.d("RemoteConfigActivity", "getData $remoteParam")
        }

        bt_remoteConfigSetCache.setOnClickListener{

            val timeNow = (Date().time / 1000).toString()
            val timeNowPlusExpireTime = Integer.parseInt(timeNow, 10) + cacheExpirationMinute.toInt()
            expired = preferences.getIntSP(this, "remoteConfigCache")
            if(expired==0){
                preferences.setIntSP(this, "remoteConfigCache", timeNowPlusExpireTime)

                mFirebaseRemoteConfig.fetch(cacheExpirationMinute).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("RemoteConfigActivity", "fetch succeeded")
                        mFirebaseRemoteConfig.fetchAndActivate()
                        bt_remoteConfigSetCache.visibility = View.GONE
                        cacheRemote()
                    } else {
                        Log.d("RemoteConfigActivity", "fetch failed")
                    }
                    Log.d("RemoteConfigActivity", "show ${task.result}")
                }
            }
        }

    }

    private fun cacheRemote(){
        expired = preferences.getIntSP(this, "remoteConfigCache")
        if(expired>0){
            tv_timer_cache.visibility = View.VISIBLE
            bt_remoteConfigSetCache.visibility = View.GONE
            val timeNowCalculate = (Date().time / 1000).toString()
            val timeNowPlusExpireTimeOnce = Integer.parseInt(timeNowCalculate, 10) + cacheExpirationMinute.toInt()
            timeNowLessTimeSaved = timeNowPlusExpireTimeOnce-expired

            Log.d("RemoteConfigActivity:", "remoteConfigCache  ${timeNowLessTimeSaved}/${cacheExpirationMinute.toInt()}")

            if(timeNowLessTimeSaved<59){
                la_timer_cache.visibility = View.VISIBLE
                la_timer_cache.setMinAndMaxFrame(timeNowLessTimeSaved, cacheExpirationMinute.toInt())
                la_timer_cache.speed = 0.029f
                la_timer_cache.playAnimation()
            }



            runnableCode = object : Runnable {
                override fun run() {
                    if(expired>0){
                        timeNowLessTimeSaved = timeNowPlusExpireTimeOnce-expired
                        if (timeNowLessTimeSaved < cacheExpirationMinute.toInt()) {
                            tv_timer_cache.text = ("${timeNowLessTimeSaved}/${cacheExpirationMinute.toInt()}")

                            Log.d("RemoteConfigActivity:", "remoteConfigCache valid  ${timeNowLessTimeSaved}/${cacheExpirationMinute.toInt()}")
                            handlerLooper.postDelayed(this, 1000)
                            expired--
                        }else{
                            handlerLooper.removeCallbacks(runnableCode!!)
                            tv_timer_cache.visibility = View.GONE
                            bt_remoteConfigSetCache.visibility = View.VISIBLE
                            la_timer_cache.visibility = View.GONE
                            preferences.setIntSP(applicationContext, "remoteConfigCache", 0)
                            mFirebaseRemoteConfig.fetch(0)
                            mFirebaseRemoteConfig.fetchAndActivate()

                            Log.d("RemoteConfigActivity:", "remoteConfigCache  reseted")
                        }

                    }
                }
            }
            handlerLooper.post(runnableCode!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(runnableCode!=null){
            la_timer_cache.cancelAnimation()
            handlerLooper.removeCallbacks(runnableCode!!)
        }

    }
}
