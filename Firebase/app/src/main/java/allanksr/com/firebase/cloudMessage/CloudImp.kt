package allanksr.com.firebase.cloudMessage

import allanksr.com.firebase.Prefs
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import org.json.JSONArray

class CloudImp (private var cloudView: CloudView): CloudPresenter {
    private var prefs = Prefs()
    override fun functionsInstance(): FirebaseFunctions {
        return FirebaseFunctions.getInstance()
    }

    override fun sendNotification(context: Context, message: ArrayList<String>) {
        if(!TextUtils.isEmpty(message[1])){
            if(prefs.getStringSP(context, "onNewToken").isNotEmpty()){
                addMessage(prefs.getStringSP(context, "onNewToken"), message)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                val e = task.exception
                                if (e is FirebaseFunctionsException) {
                                    val ex = e.message
                                    cloudView.singleResult("FirebaseFunctionsException $ex")
                                }
                            }
                        }
            }else{
                cloudView.singleResult("Exception Token does not exists")
            }
        }else{
            cloudView.singleResult("Exception message cannot be Empty")
        }


    }

    override fun sendBackgroundData(context: Context, data: ArrayList<String>) {
        if(!TextUtils.isEmpty(data[1])){
            if(prefs.getStringSP(context, "onNewToken").isNotEmpty()){
                addMessage(prefs.getStringSP(context, "onNewToken"), data)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                val e = task.exception
                                if (e is FirebaseFunctionsException) {
                                    val ex = e.message
                                    cloudView.singleResult("FirebaseFunctionsException $ex")
                                }
                            }
                        }
            }else{
                cloudView.singleResult("Exception Token does not exists")
            }
        }else{
            cloudView.singleResult("Exception data cannot be Empty")
        }

    }

    private fun addMessage(token:String, messageData: ArrayList<String>): Task<ArrayList<*>> {
        cloudView.waitResult()
        val data = hashMapOf("token" to token, "messageData" to messageData)
        return functionsInstance().getHttpsCallable("pushData").call(data)
                .continueWith { task ->
                    val getData = task.result?.data as HashMap<*, *>
                    val dataResult = getData["data"] as ArrayList<*>
                    cloudView.singleResult("${dataResult[0]}")
                    dataResult
                }
    }

}