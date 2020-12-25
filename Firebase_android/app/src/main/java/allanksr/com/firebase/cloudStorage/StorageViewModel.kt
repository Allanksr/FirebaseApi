package allanksr.com.firebase.cloudStorage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage

class StorageViewModel private constructor(private val storageRepository: StorageRepository) : ViewModel() {

    fun storageGetInstance(): FirebaseStorage {
       return storageRepository.storageInstance()
    }

    fun uploadFile(context: Context, uri: Uri) {
        return storageRepository.uploadFile(context, uri)
    }

    fun listFiles() {
        return storageRepository.listFiles()
    }

    fun deleteFile(fileName: String) {
        return storageRepository.deleteFile(fileName)
    }

    @Suppress("UNCHECKED_CAST")
    class StorageViewModelFactory(private val storageRepository: StorageRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StorageViewModel(storageRepository) as T
        }
    }
}