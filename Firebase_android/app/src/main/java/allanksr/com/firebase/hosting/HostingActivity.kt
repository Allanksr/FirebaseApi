package allanksr.com.firebase.hosting

import allanksr.com.firebase.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.hosting_activity.*
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


class HostingActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hosting_activity)
        handleSSLHandshake()
        bt_request.setOnClickListener{
            jsonObjectRequest(this, "https://fir-2870a.web.app/android")
        }

    }
    @Suppress("SameParameterValue")
     private fun jsonObjectRequest(context: Context, hostingUrl: String){
        val queue = Volley.newRequestQueue(context)
        val params = "$hostingUrl?auth=myCredential&name=Android"


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, params, "",
                {
                    tv_status.text = "${it["status"]}"
                    tv_result.text = "${it["optional"]}"
                   Log.d("HostingActivity", " status ${it["status"]} optional ${it["optional"]}")
                }
        ) {
            tv_result.text = "$it"
            Log.d("HostingActivity", "error $it")
        }
        queue.add(jsonObjectRequest)
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(jsonObjectRequest)
    }

    //Enable https connections | Not do this in production
    private fun handleSSLHandshake() {
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }
}
