package allanksr.com.firebase.authentication.mvpPlayGames

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import allanksr.com.firebase.constantes
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.playgames_layout.*

class PlayGames : AppCompatActivity(), PlayGamesView, PreventExit {
    private lateinit var playGamesPresenter: PlayGamesPresenter
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playgames_layout)

        playGamesPresenter = PlayGamesImp(this, this)
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                        .requestServerAuthCode(constantes.default_web_client_id)
                        .build()
               )
        btn_connect.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

    }

    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
       playGamesPresenter.signIn(this, result)
    }

    public override fun onStart() {
        super.onStart()
        playGamesPresenter.firebaseAuth()
    }
    public override fun onStop() {
        super.onStop()
        playGamesPresenter.firebaseInstance().removeAuthStateListener{}
    }

    override fun onSignIn(name: String, photo: String) {
        Glide.with(this).load(photo).into(iv_profile)
        tv_name.text = name
        rl_sigin.visibility = View.GONE
        rl_welcome.visibility = View.VISIBLE
        btn_signout.setOnClickListener{
            playGamesPresenter.signOut()
        }
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun onSignOut() {
        mGoogleSignInClient.signOut()
        tv_playgames_result.text = ""
        rl_sigin.visibility = View.VISIBLE
        rl_welcome.visibility = View.GONE
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun onFail(fail: String) {
        Log.d("OnFail:", fail)
        tv_playgames_result.text = fail
    }

    override fun waitResult() {
        tv_playgames_result.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun waitToExit() {
        Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        this.finish()
    }

    override fun onBackPressed() {
        playGamesPresenter.exitActivity()
    }

}