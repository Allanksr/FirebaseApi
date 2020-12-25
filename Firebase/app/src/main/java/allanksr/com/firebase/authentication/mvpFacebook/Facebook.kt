package allanksr.com.firebase.authentication.mvpFacebook

import allanksr.com.firebase.R
import allanksr.com.firebase.authentication.mvpFacebook.FacebookFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class Facebook: AppCompatActivity() {
    private lateinit var facebookFragment : FacebookFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facebook_layout)

        facebookFragment =  FacebookFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.facebookFragmentComponent, facebookFragment)
            .commit()
    }
    override fun onBackPressed() {
        val fragment = facebookFragment
        fragment.facebookPresenter.exitFragment()
    }

    /*
     private fun generateFbFingerPrint() {
         try {
             val info = packageManager.getPackageInfo(
                 "allanksr.com.firebase",
                 PackageManager.GET_SIGNATURES
             )
             for (signature in info.signatures) {
                 val md: MessageDigest = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 val sign: String = Base64
                     .encodeToString(md.digest(), Base64.DEFAULT)
                 Log.e("KEYHASH:", sign)
                 Toast.makeText(applicationContext, sign, Toast.LENGTH_LONG)
                     .show()
             }
         } catch (e: PackageManager.NameNotFoundException) {
             e.printStackTrace()
         } catch (e: NoSuchAlgorithmException) {
             e.printStackTrace()
         }
     }
     */

}