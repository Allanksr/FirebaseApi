package allanksr.com.firebase.realtimeDatabase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataGet(
        var position: Int? = 0,
        var id: String? = "",
        var name: String? = "",
        var email: String? = "",
        var imageUrl: String? = ""
)