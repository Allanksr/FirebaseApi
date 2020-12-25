package allanksr.com.firebase.realtimeDatabase

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataSet(
        var name: String? ="",
        var email: String? ="",
        var imageUrl: String? =""
){
        @Exclude
        fun toMap(): Map<String, Any?> {
                return mapOf(
                        "name" to name,
                        "email" to email,
                        "imageUrl" to imageUrl
                )
        }
}
