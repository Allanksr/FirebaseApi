package allanksr.com.firebase.authentication.mvpMicrosoft

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.microsoft_layout.*
import java.security.SecureRandom
import javax.net.ssl.*

class Microsoft : AppCompatActivity(), MicrosoftView, PreventExit {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var microsoftPresenter : MicrosoftPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.microsoft_layout)

        microsoftPresenter = MicrosoftImp(this, this)
        btn_connect.setOnClickListener{
            microsoftPresenter.microsoftLogin(this)
        }

    }


    public override fun onStart() {
        super.onStart()
        microsoftPresenter.firebaseAuth(this)
    }

    override fun onFail(fail: String) {
        tv_microsoft_result.text = fail
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun waitResult() {
        tv_microsoft_result.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccess(name: String, image: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_microsoft_result.text = ""
        rl_sigin.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        Glide.with(this).load(image).into(iv_profile)
        tv_name.text = name
        btn_signout.setOnClickListener{
            microsoftPresenter.signOut()
        }
    }

    override fun onSignOut() {
        rl_sigin.visibility = View.VISIBLE
        rl_welcome.visibility = View.GONE
    }


    //Enables https connections | Not do this in production
    override fun handleSSLHandshake() {
        try {
            val trustAllCerts: Array<TrustManager> =
                arrayOf(object : X509TrustManager {

                    override fun checkClientTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) {
                        Log.d("microsoftSuccess", "$authType")
                    }

                    override fun checkServerTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) {
                        Log.d("microsoftSuccess", "$authType")
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        Log.d("microsoftSuccess", "getAcceptedIssuers")
                        return arrayOf()
                    }
                })
            val sc: SSLContext = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }

        } catch (ignored: java.lang.Exception) {
            Log.d("microsoftSuccess", "$ignored")
        }
    }

    override fun findProvider() {
        edt_email.visibility = View.VISIBLE
        btn_connect.text = getString(R.string.provider)
        btn_connect.setBackgroundResource(R.drawable.button_red)
        btn_connect.setOnClickListener{
            microsoftPresenter.findProvider(this, edt_email.text.toString())
        }
    }

    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        microsoftPresenter.exitActivity()
    }
}