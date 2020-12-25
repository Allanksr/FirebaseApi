package allanksr.com.firebase.authentication.mvpAnonymous

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.mvpAnonymous.AnonymousPresenter
import allanksr.com.firebase.authentication.mvpAnonymous.AnonymousImp
import allanksr.com.firebase.authentication.mvpAnonymous.AnonymousView
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.anonymous_layout.*
import kotlinx.android.synthetic.main.loading.*


class Anonymous: AppCompatActivity(), AnonymousView {
    private lateinit var anonymousPresenter : AnonymousPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anonymous_layout)
        anonymousPresenter = AnonymousImp(this)

        btn_connect.setOnClickListener{
            anonymousPresenter.anonymousLogin(this)
        }

    }


    override fun onSuccess(name: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_anonymous_result.text = ""
        rl_sigin.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        tv_name.text = name
        btn_signout.setOnClickListener{
            anonymousPresenter.signOut()
        }
    }

    override fun onFail(fail: String) {
        tv_anonymous_result.text = fail
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun signOut() {
        rl_sigin.visibility = View.VISIBLE
        rl_welcome.visibility = View.GONE
    }

    override fun waitResult() {
        tv_anonymous_result.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()
        anonymousPresenter.firebaseAuth(this)
    }

}