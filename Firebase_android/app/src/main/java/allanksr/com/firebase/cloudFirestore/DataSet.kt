package allanksr.com.firebase.cloudFirestore

data class DataSet(
        var name: String,
        var email: String,
        var imageUrl: String
){
        fun toMap(): Map<String, Any> {
                return mapOf(
                        "name" to name,
                        "email" to email,
                        "imageUrl" to imageUrl
                )
        }
}
