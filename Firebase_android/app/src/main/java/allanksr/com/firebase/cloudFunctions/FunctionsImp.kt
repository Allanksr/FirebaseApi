package allanksr.com.firebase.cloudFunctions

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import org.json.JSONArray

class FunctionsImp(private var functionsView: FunctionsView): FunctionsPresenter {

    override fun functionsInstance(): FirebaseFunctions {
        return FirebaseFunctions.getInstance()
    }

    override fun singleCall() {
        functionsView.waitResult()
        functionsInstance().getHttpsCallable("functionsData").call()
        .continueWith { task ->
            if(task.isSuccessful){
                val getData = task.result?.data as HashMap<*, *>
                val dataResult = getData["data"] as ArrayList<*>
                functionsView.singleResult("${dataResult[1]}")
            }
        }
    }

    override fun callWithData(coinType: String) {
        addMessage(coinType)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val ex = e.message
                            functionsView.singleResult("FirebaseFunctionsException $ex")
                        }
                    }
                }
    }


    private fun addMessage(coinType: String): Task<ArrayList<*>> {
        functionsView.waitResult()
        val data = hashMapOf("data" to coinType)
       return functionsInstance().getHttpsCallable("functionsData").call(data)
        .continueWith { task ->
            val getData = task.result?.data as HashMap<*, *>
            val dataResult = getData["data"] as ArrayList<*>
             try {
                val jsonObject = JSONArray("${dataResult[1]}")
                 val name = jsonObject.getJSONObject(0)["name"]
                 val high = jsonObject.getJSONObject(0)["high"]
                 functionsView.singleResult("1-${name} = R$${high} ")
            } catch (e: Exception) {
                 functionsView.singleResult("Exception ${e.message}")
            }
            dataResult
        }
    }

}