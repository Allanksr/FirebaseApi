package allanksr.com.firebase.cloudStorage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class StorageRepository(private var storageView: StorageView) {

      fun storageInstance(): FirebaseStorage {
         return FirebaseStorage.getInstance("gs://fir-2870a.appspot.com")
       }

     private val mainRef = storageInstance()

    fun uploadFile(context: Context, uri: Uri) {
        storageView.waitResult()
       val fileNme = fileNameAndType(context, uri)
       val ref = mainRef.reference.child(fileNme)
       val uploadTask = ref.putFile(uri)

        uploadTask.addOnProgressListener {
            val progress = (100 * it.bytesTransferred) / it.totalByteCount
            storageView.setProgress("Upload is $progress% done")
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    storageView.setError("Failure : ${it.message}")
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                storageView.addData(fileNme, "$downloadUri")
            } else {
                task.addOnFailureListener {
                    storageView.setError("Failure : ${it.message}")
                }
            }
        }

    }

    private fun fileNameAndType(context: Context, uri: Uri): String {
        val file = File(uri.toString())
        var filePath: String = file.name


        if(filePath.contains("%2F")){
            filePath = filePath.substring(filePath.indexOf("%2F"))
            while (filePath.contains("%2F")) {
                filePath = filePath.replace("%2F", "")
            }
            Log.d("uploadTask", "substr : $filePath")
        }


        val extension: String?
        //Check uri format to avoid null
        extension = if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters.
            // This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path!!)).toString())
        }
        return "$filePath.$extension"
    }


         fun listFiles(){
              //mainRef.reference.list(3)
               mainRef.reference.listAll()
                    .addOnSuccessListener { it ->
                        for(i in it.items){
                           mainRef.reference.child(i.name).downloadUrl.addOnSuccessListener {uri ->
                               Log.d("uploadTask", "downloadUrl : $uri")
                               storageView.getData(i.name, "$uri")

                           }.addOnFailureListener {
                               storageView.setError("Failure : ${it.message}")
                           }
                       }
                    }
                    .addOnFailureListener {
                        storageView.setError("Failure : ${it.message}")
                    }

          }


        fun deleteFile(fileName: String){
            mainRef.reference.child(fileName).delete()
        }

}