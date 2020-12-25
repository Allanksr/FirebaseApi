package allanksr.com.firebase.cloudMessage

import android.content.Context
import com.google.firebase.functions.FirebaseFunctions

interface CloudPresenter {
    fun functionsInstance(): FirebaseFunctions
    fun sendNotification(context: Context, message: ArrayList<String>)
    fun sendBackgroundData(context: Context, data: ArrayList<String>)
}