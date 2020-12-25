package allanksr.com.firebase.admob

import allanksr.com.firebase.R
import allanksr.com.firebase.constantes
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.admob_activity.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.mob.*
import kotlinx.android.synthetic.main.protect_ad.*


class AdmobActivity: AppCompatActivity() {
    private lateinit var adView: AdView
    lateinit var interstitialAd: InterstitialAd
    private var rewardedAd: RewardedAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admob_activity)

        MobileAds.initialize(this){}

        val adRequest = AdRequest.Builder().build()
        adView = AdView(this)
        adView.windowSystemUiVisibility

        bt_banner.setOnClickListener{
            loading()
            adMob.loadAd(adRequest)
            adMob.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    val animationRight = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.from_right
                    )
                   loaded()
                    include_admob?.startAnimation(animationRight)
                    protectAd.setOnClickListener{
                        Toast.makeText(applicationContext, "See ads", Toast.LENGTH_SHORT).show()
                        protectAd.visibility = View.GONE
                    }
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    loaded()
                    Log.d("AdmobActivity", "LoadAdError $p0")
                }
            }
        }

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = constantes.firebaseInterstitialTEST
        bt_interstitial.setOnClickListener{
            loading()
            interstitialAd.loadAd(AdRequest.Builder().build())

            interstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    loaded()
                    interstitialAd.show()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loaded()
                    Log.d("AdmobActivity", "LoadAdError $adError")
                }

                override fun onAdOpened() {
                    loaded()
                    Log.d("AdmobActivity", "onAdOpened")
                }

                override fun onAdClicked() {
                    loaded()
                    Log.d("AdmobActivity", "onAdClicked")
                }

                override fun onAdLeftApplication() {
                    loaded()
                    Log.d("AdmobActivity", "onAdLeftApplication")
                }

                override fun onAdClosed() {
                    loaded()
                    Log.d("AdmobActivity", "onAdClosed")
                }
            }
        }

        bt_video_reward.setOnClickListener{
            rewardedAd =  RewardedAd(this, constantes.firebaseRewardTEST)
            loading()
            val adLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
                override fun onRewardedAdLoaded() {
                    loaded()

                    val adCallback: RewardedAdCallback = object : RewardedAdCallback() {
                        override fun onRewardedAdOpened() {
                            Log.d("AdmobActivity", "onRewardedAdOpened")
                        }

                        override fun onRewardedAdClosed() {
                            Log.d("AdmobActivity", "onRewardedAdClosed")
                        }

                        override fun onUserEarnedReward(reward: RewardItem) {
                            Log.d("AdmobActivity", "onUserEarnedReward ${reward.amount}")
                        }

                        override fun onRewardedAdFailedToShow(adError: AdError) {
                            Log.d("AdmobActivity", "onRewardedAdFailedToShow $adError")
                        }
                    }

                    rewardedAd!!.show(this@AdmobActivity, adCallback)


                }

                override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                    loaded()
                    Log.d("AdmobActivity", "LoadAdError $adError")
                }
            }
            rewardedAd!!.loadAd(adRequest, adLoadCallback)




        }

    }

    fun loading(){
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
    }

    fun loaded(){
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

}
