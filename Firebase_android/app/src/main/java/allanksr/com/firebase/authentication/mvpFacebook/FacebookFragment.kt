package allanksr.com.firebase.authentication.mvpFacebook

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.PreventExit
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.facebook_fragment.view.*
import kotlinx.android.synthetic.main.loading.view.*


class FacebookFragment: Fragment(), FacebookView, PreventExit {
    private lateinit var callbackManager : CallbackManager
    lateinit var facebookPresenter: FacebookPresenter
    private var signinRl : RelativeLayout? = null
    private var facebookImage: ImageView? = null
    private var faceSignin: Button? = null
    private var welcome : RelativeLayout? = null
    private var profileImage: ImageView? = null
    private var profileName: TextView? = null
    private var profileEmail: TextView? = null
    private var faceSignout: Button? = null
    private var include: View ?= null
    private var facebookResult: TextView? = null
    private var loading: LottieAnimationView? = null
    private val email = "email"
    private val publicProfile = "public_profile"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.facebook_fragment, container, false)
        facebookPresenter = FacebookImp(this, this)
        callbackManager = CallbackManager.Factory.create()
        facebookPresenter.loginManager(this, callbackManager)
        signinRl = view.rl_signin as RelativeLayout
        facebookImage = view.iv_facebook as ImageView
        faceSignin = view.btn_signin as Button
        welcome = view.rl_welcome as RelativeLayout
        profileImage = view.iv_profile as ImageView
        profileName = view.tv_name as TextView
        profileEmail = view.tv_email as TextView
        faceSignout = view.btn_signout as Button
        include = view.include_loading as View
        facebookResult = view.tv_facebook_result as TextView
        loading = view.la_loading as LottieAnimationView
        faceSignin?.setOnClickListener{
           LoginManager.getInstance().logInWithReadPermissions(this, listOf(email, publicProfile))

        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSignIn(name: String, email: String, photo: String) {
        facebookResult?.text = ""
        signinRl?.visibility = View.GONE
        welcome?.visibility = View.VISIBLE
        Glide.with(this).load(photo).into(profileImage!!)
        profileName?.text = name
        profileEmail?.text = email
        faceSignout?.setOnClickListener{
            facebookPresenter.signOut()
        }
        include?.visibility = View.GONE
        loading?.cancelAnimation()
    }

    override fun onSignOut() {
        facebookResult?.text = ""
        signinRl?.visibility = View.VISIBLE
        welcome?.visibility = View.GONE
        include?.visibility = View.GONE
        loading?.cancelAnimation()
    }

    override fun onFail(fail: String) {
        include?.visibility = View.GONE
        loading?.cancelAnimation()
        facebookResult?.text = fail
    }

    override fun waitResult() {
        facebookResult?.text = ""
        include?.isClickable = true
        include?.visibility = View.VISIBLE
        loading?.playAnimation()
        include?.setOnClickListener{
            Toast.makeText(activity, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

     override fun onStart() {
        super.onStart()
         facebookPresenter.firebaseAuth()
    }

    override fun waitToExit() {
        Toast.makeText(activity, getString(R.string.wait), Toast.LENGTH_SHORT).show()
    }

    override fun exitActivity() {
        activity?.finish()
    }

}