package allanksr.com.firebase.cloudMessage

interface CloudView {
    fun waitResult()
    fun singleResult(result: String)
}