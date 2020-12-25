package allanksr.com.firebase.authentication.mvpSmartphone

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.phone_layout.*

class Phone : AppCompatActivity(), PhoneView, PreventExit {
    private lateinit var phonePresenter : PhonePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_layout)

        phonePresenter = PhoneImp(this, this)
        bt_sign_up.setOnClickListener{
            phonePresenter.verifyPhoneNumber(edt_PhoneNumber.text.toString(), this)
        }

        bt_sign_in.setOnClickListener{
            phonePresenter.verifyPhoneNumber(edt_PhoneNumber.text.toString(), this)
        }

    }

    override fun signInWithCredentialSuccess(success: String) {
        rl_code_confirm.visibility = View.GONE
        rl_create_account.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        tv_user_data.text = success
        btn_disconect.setOnClickListener{
            phonePresenter.signOut()
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun signInWithCredentialFail(fail: String) {
        rl_code_confirm.visibility = View.GONE
        rl_create_account.visibility = View.GONE
        rl_welcome.visibility = View.GONE
        tv_phone_result.text = fail
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun firebaseUser(phoneNumber: String) {
        tv_phone_result.text = ""
        rl_welcome.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        tv_user_data.text = phoneNumber
        btn_disconect.setOnClickListener{
            phonePresenter.signOut()
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun onVerificationFailed(error: String) {
        tv_phone_result.text = error
    }

    override fun confirmCode() {
        rl_code_confirm.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        bt_confirm.setOnClickListener{
            phonePresenter.receivedCode(this, edt_verificationCode.text.toString())
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun firebaseAuth() {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        rl_welcome.visibility = View.GONE
        rl_create_account.visibility = View.VISIBLE
    }

    override fun firebaseSignOut() {
        rl_welcome.visibility = View.GONE
        rl_create_account.visibility = View.VISIBLE
    }

    override fun waitResult() {
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun isVerifyingPhoneNumber() {
        rl_code_confirm.visibility = View.VISIBLE
        rl_create_account.visibility = View.GONE
        bt_confirm.setOnClickListener{
            phonePresenter.receivedCode(this, edt_verificationCode.text.toString())
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    public override fun onStart() {
        super.onStart()
        phonePresenter.firebaseAuth(this)
    }


    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        phonePresenter.exitActivity()
    }

}