package  allanksr.com.firebase.cloudMessage

import allanksr.com.firebase.R
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DisplayAlert : AppCompatActivity() {
    private var title: String? = null
    private var body: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = intent.extras
        if (b != null) {
            title = b!!.getString("title")
            body = b!!.getString("body")
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(title)
            builder.setIcon(R.drawable.firebase)
            builder.setMessage(body)
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    finish()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
    companion object {
        var b: Bundle? = null
    }
}