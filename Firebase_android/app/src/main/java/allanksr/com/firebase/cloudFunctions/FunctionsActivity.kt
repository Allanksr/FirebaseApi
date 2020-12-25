package allanksr.com.firebase.cloudFunctions

import allanksr.com.firebase.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.functions_activity.*
import kotlinx.android.synthetic.main.functions_activity.include_loading
import kotlinx.android.synthetic.main.loading.*


class FunctionsActivity: AppCompatActivity(), FunctionsView, AdapterView.OnItemSelectedListener{
    private lateinit var functionsPresenter: FunctionsPresenter
    private var touchEvent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.functions_activity)
        functionsPresenter = FunctionsImp(this)

        bt_single_request.setOnClickListener{
            functionsPresenter.singleCall()
        }

        ArrayAdapter.createFromResource(this, R.array.coins_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            coin_choice.adapter = adapter
        }
        coin_choice.onItemSelectedListener = this

    }

    override fun waitResult() {
        tv_response.visibility = View.GONE
        tv_response.text = ""
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

    override fun singleResult(result: String) {
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
        tv_response.visibility = View.VISIBLE
        tv_response.text = result
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if(touchEvent){
            val coin: String = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, coin, Toast.LENGTH_SHORT).show()
            functionsPresenter.callWithData(coin)
        }
        touchEvent = true
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
