package allanksr.com.firebase.cloudFirestore


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireStoreRepository(private var fireStoreView: FireStoreView) {
     var data = DataGet("", "", "", "")
     private var dataId :String = ""

     private fun fireStoreInstance():FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

     fun setData(data: DataSet){
         if(data.name.isEmpty() || data.email.isEmpty() || data.imageUrl.isEmpty()){
             fireStoreView.setError("fill all the fields")
         }else{
             val test = fireStoreInstance().collection("test")
             val postValues = data.toMap()
             test.add(postValues)
                     .continueWith {
                         dataId = it.result.id
                         fireStoreView.waitResult()
                         fireStoreView.getData()
                     }
         }
     }

   suspend fun getDoc(): Task<DataGet> {
       return withContext(Dispatchers.Default) {
           val test = fireStoreInstance().collection("test").document(dataId)
           test.get().continueWith {
               if (it.isSuccessful) {
                   data = DataGet(
                           dataId,
                           "${it.result["name"]}",
                           "${it.result["email"]}",
                           "${it.result["imageUrl"]}"
                   )
               }
               data
           }
       }
     }

    suspend fun getDocSaved(docId: String): Task<DataGet> {
        return withContext(Dispatchers.Default) {
            val test = fireStoreInstance().collection("test").document(docId)
            test.get().continueWith {
                if (it.isSuccessful) {
                    data = DataGet(
                            docId,
                            "${it.result["name"]}",
                            "${it.result["email"]}",
                            "${it.result["imageUrl"]}"
                    )
                }
                data
            }
        }
    }


    fun updateData(docId: String, data: DataSet){
        if(data.name.isEmpty() || data.email.isEmpty() || data.imageUrl.isEmpty()){
            fireStoreView.setError("fill all the fields")
        }else{
            val test = fireStoreInstance().collection("test").document(docId)
            val postValues = data.toMap()
            test.set(postValues)
                    .continueWith {
                        dataId = docId
                        fireStoreView.waitResult()
                        fireStoreView.getData()
                    }
        }
    }



     fun deleteDoc(docId: String) {
       fireStoreInstance().collection("test").document(docId).delete()
    }


}