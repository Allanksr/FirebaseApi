package allanksr.com.firebase.authentication.mvpGoogle

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import allanksr.com.firebase.constantes
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.google_layout.*
import kotlinx.android.synthetic.main.loading.*


class Google : AppCompatActivity(), GoogleView, PreventExit {
    private lateinit var googlePresenter: GooglePresenter
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.google_layout)

        googlePresenter = GoogleImp(this, this)

        mGoogleSignInClient = GoogleSignIn.getClient(this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(constantes.default_web_client_id)
                    .requestEmail()
                    .build()
                )

        btn_connect.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
       googlePresenter.signIn(this, result.data!!)
    }

    public override fun onStart() {
        super.onStart()
        googlePresenter.firebaseAuth()
    }

    override fun onSignIn(name: String, email: String, photo: String) {
        val remountUrl = photo.substring(0, photo.lastIndex-5)
        Glide.with(this).load(remountUrl).into(iv_profile)
        tv_name.text = name
        tv_email.text = email
        rl_sigin.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        btn_signout.setOnClickListener{
            googlePresenter.firebaseSignOut()
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()

        iv_profile.setOnClickListener{
            rl_enlarged_image.isClickable = true
            rl_enlarged_image.visibility = View.VISIBLE
            Glide.with(this).load(remountUrl).into(iv_google_enlarged)
            btn_close_image.setOnClickListener{
                rl_enlarged_image.visibility = View.GONE
                Glide.with(this).load("").into(iv_google_enlarged)
            }

        }

    }


    override fun onSignOut() {
        mGoogleSignInClient.signOut()
        tv_google_result.text = ""
        rl_sigin.visibility = View.VISIBLE
        rl_welcome.visibility = View.GONE
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun onFail(fail: String) {
        tv_google_result.text = fail
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun waitResult() {
        tv_google_result.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        googlePresenter.exitActivity()
    }

}