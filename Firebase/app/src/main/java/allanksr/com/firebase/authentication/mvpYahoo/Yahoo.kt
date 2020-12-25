package allanksr.com.firebase.authentication.mvpYahoo

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.yahoo_layout.*


class Yahoo : AppCompatActivity(), YahooView, PreventExit {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var yahooPresenter : YahooPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yahoo_layout)

        yahooPresenter = YahooImp(this, this)

        btn_connect.setOnClickListener{
            yahooPresenter.yahooLogin(this)
        }
    }

    public override fun onStart() {
        super.onStart()
        yahooPresenter.firebaseAuth(this)
    }

    override fun onFail(fail: String) {
        tv_yahoo_result.text = fail
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun waitResult() {
        tv_yahoo_result.text = ""
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
        tv_yahoo_result.text = ""
        rl_sigin.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        Glide.with(this).load(image).into(iv_profile)
        tv_name.text = name
        btn_signout.setOnClickListener{
            yahooPresenter.signOut()
        }
    }

    override fun onSignOut() {
        rl_sigin.visibility = View.VISIBLE
        rl_welcome.visibility = View.GONE
    }

    override fun findProvider() {
        edt_email.visibility = View.VISIBLE
        btn_connect.text = getString(R.string.provider)
        btn_connect.setBackgroundResource(R.drawable.button_red)
        btn_connect.setOnClickListener{
            yahooPresenter.findProvider(this, edt_email.text.toString())
        }
    }

    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        yahooPresenter.exitActivity()
    }

}