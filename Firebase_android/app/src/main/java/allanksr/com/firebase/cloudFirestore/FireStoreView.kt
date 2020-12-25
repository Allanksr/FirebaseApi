package allanksr.com.firebase.cloudFirestore

interface FireStoreView {
    fun waitResult()
    fun setError(error: String)
    fun getData()
}