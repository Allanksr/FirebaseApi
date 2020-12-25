package allanksr.com.firebase.realtimeDatabase


import android.util.Log
import com.google.firebase.database.FirebaseDatabase

class DatabaseRepository(private var databaseView: DatabaseView) {

      fun firebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

     fun setData(data: DataSet){
         if(data.name!!.isEmpty() || data.email!!.isEmpty() || data.imageUrl!!.isEmpty()){
             databaseView.setError("fill all the fields")
         }else{

             val postValues = data.toMap()
             val onWrite = firebaseDatabaseInstance()
                     .reference
                     .child("users")
                     .push()
                     .setValue(postValues)
             onWrite.addOnSuccessListener {
                 Log.d("SuccessListener","SUCESS")
                 databaseView.getData()
             }
             onWrite.addOnFailureListener{
                 Log.d("FailureListener","FAILURE")
                 databaseView.setError("${it.message}")
             }


         }
     }

    fun updateData(userId: String, data: DataSet){
        if(data.name!!.isEmpty() || data.email!!.isEmpty() || data.imageUrl!!.isEmpty()){
            databaseView.setError("fill all the fields")
        }else{
            val postValues = data.toMap()
            val onUpdate = firebaseDatabaseInstance()
                    .reference
                    .child("users")
                    .child(userId)
                    .updateChildren(postValues)
            onUpdate.addOnSuccessListener {
                Log.d("SuccessListener","SUCESS")
                databaseView.getData()
            }
            onUpdate.addOnFailureListener{
                Log.d("FailureListener","FAILURE")
                databaseView.setError("${it.message}")
            }
        }
    }


    fun deleteData(userId: String){
        Log.d("deleteData"," :$userId")
        firebaseDatabaseInstance()
                .reference
                .child("users/$userId/")
                .removeValue()
    }



}