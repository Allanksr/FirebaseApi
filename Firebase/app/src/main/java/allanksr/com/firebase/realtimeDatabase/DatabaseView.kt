package allanksr.com.firebase.realtimeDatabase

interface DatabaseView {
    fun waitResult()
    fun setError(error: String)
    fun getData()
}