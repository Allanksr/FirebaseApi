package allanksr.com.firebase.cloudFirestore

import allanksr.com.firebase.Prefs
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FireStoreViewModel private constructor(private val mFireStoreRepository: FireStoreRepository) : ViewModel() {
    val fireStoreData = MutableLiveData<Task<DataGet>>()
    private var preferences = Prefs()
    fun addUser(data: DataSet){
        mFireStoreRepository.setData(data)
    }

    fun getDoc(){
        CoroutineScope(Dispatchers.Main).launch {
          val data = withContext(Dispatchers.Default){
                mFireStoreRepository.getDoc()
            }
            fireStoreData.value = data
        }
    }

    fun getDocSaved(context: Context){
        val docId = preferences.getStringSP(context, "docId")
        if(docId.isNotEmpty()){
            CoroutineScope(Dispatchers.Main).launch {
                val data = withContext(Dispatchers.Default){
                    mFireStoreRepository.getDocSaved(docId)
                }
                fireStoreData.value = data
            }
        }
    }

    fun updateDoc(docId: String, data: DataSet){
        mFireStoreRepository.updateData(docId, data)
    }

    fun deleteDoc(docId: String){
        mFireStoreRepository.deleteDoc(docId)
    }

    @Suppress("UNCHECKED_CAST")
    class FireStoreViewModelFactory(private val mFireStoreRepository: FireStoreRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FireStoreViewModel(mFireStoreRepository) as T
        }
    }
}