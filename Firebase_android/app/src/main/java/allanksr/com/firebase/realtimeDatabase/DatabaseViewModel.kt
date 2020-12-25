package allanksr.com.firebase.realtimeDatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference

class DatabaseViewModel private constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {

    fun databaseGetInstance(): DatabaseReference {
       return databaseRepository.firebaseDatabaseInstance().reference
    }

    fun addUser(data: DataSet){
        databaseRepository.setData(data)
    }

    fun updateDoc(userId: String, data: DataSet){
        databaseRepository.updateData(userId, data)
    }

    fun deleteDoc(userId: String){
       databaseRepository.deleteData(userId)
    }

    @Suppress("UNCHECKED_CAST")
    class DatabaseViewModelFactory(private val databaseRepository: DatabaseRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DatabaseViewModel(databaseRepository) as T
        }
    }
}