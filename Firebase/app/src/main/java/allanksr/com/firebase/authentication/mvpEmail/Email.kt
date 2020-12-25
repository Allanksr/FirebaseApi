package allanksr.com.firebase.authentication.mvpEmail

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.email_layout.*
import kotlinx.android.synthetic.main.email_layout.include_loading
import kotlinx.android.synthetic.main.email_layout.rl_welcome
import kotlinx.android.synthetic.main.loading.*


class Email : AppCompatActivity(), MailView, PreventExit {
    var preferences = Prefs()
    private lateinit var emailPresenter : EmailPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_layout)

        emailPresenter = EmailImp(this,this)
        bt_sign_up.setOnClickListener {
            emailPresenter.createAccountWithEmail(this, edt_email.text.toString(), edt_userPass.text.toString())
        }
        bt_sign_in.setOnClickListener {
            emailPresenter.siginWithEmail(this, edt_email.text.toString(), edt_userPass.text.toString())
        }

        bt_sign_in_without_password.setOnClickListener {
            emailPresenter.siginWithWithoutPassword(this, edt_email.text.toString())
        }

    }

    override fun firebaseCreateUserWithEmailSuccess(success: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_email_result.text = ""
        rl_welcome.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        tv_user_data.text = success
        btn_disconect.setOnClickListener{
            emailPresenter.firebaseSignOut()
        }
    }

    override fun firebaseCreateUserWithEmailFail(fail: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_email_result.text = fail
    }

    override fun firebaseSiginWithEmailSuccess(success: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_email_result.text = ""
        rl_welcome.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        tv_user_data.text = success
        btn_disconect.setOnClickListener{
            emailPresenter.firebaseSignOut()
        }
    }

    override fun firebaseSiginWithEmailFail(fail: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_email_result.text = fail
    }

    override fun firebaseUser(userEmail: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_email_result.text = ""
        rl_welcome.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        tv_user_data.text = userEmail
        btn_disconect.setOnClickListener{
          emailPresenter.firebaseSignOut()
        }
    }

    override fun checkEmail(checkEmail: String, email: String) {
        rl_welcome.visibility = View.GONE
        rl_create_account.visibility = View.GONE
        tv_email_result.text = checkEmail
        preferences.setStringSP(this, "email", email)
    }

    override fun viewSignOut() {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        rl_welcome.visibility = View.GONE
        rl_create_account.visibility = View.VISIBLE
    }

    override fun waitResult() {
        tv_email_result.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

    override fun resetPassword() {
        bt_sign_in_without_password.text = getString(R.string.does_not_have_password)
        bt_sign_in_without_password.setBackgroundResource(R.drawable.button_red)
        bt_sign_in_without_password.setOnClickListener {
            emailPresenter.resetPasswordLink(this, edt_email.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        emailPresenter.firebaseAuth()
    }

    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        emailPresenter.exitActivity()
    }


}