package allanksr.com.firebase.cloudStorage

interface StorageView {
    fun waitResult()
    fun setError(error: String)
    fun setProgress(progress: String)
    fun getData(fileName: String, fileUrl: String)
    fun addData(fileName: String, fileUrl: String)
}