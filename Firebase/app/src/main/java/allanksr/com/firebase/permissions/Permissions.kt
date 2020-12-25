package allanksr.com.firebase.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class Permissions {
    private val appFolder = "firebaseFolder"
    private var readInternalExternalStorage: Boolean = false
    private var cameraPermission: Boolean = false
    private val cameraRequestCode = 1000
    private val readInternalExternalStorageRequestCode = 4000
    private val writeInternalExternalStorageRequestCode = 5000

    fun requestCamera(ctx: Context, atv: AppCompatActivity): Boolean {
        if (SDK_INT < 23) {
            cameraPermission = true
        }else{

            val permissionCamera = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
            if (permissionCamera!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(atv, arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
            }else{
                cameraPermission = true
            }
        }
        return cameraPermission
    }

    fun readwriteStorage(ctx: Context, atividade: AppCompatActivity): Boolean {
        var folder: File
        if (SDK_INT < 23) {
            readInternalExternalStorage = true
            @Suppress("DEPRECATION")
            folder = File(Environment.getExternalStorageDirectory(), appFolder)
            if (!folder.exists()) {
                folder.mkdir()
            }
        } else {
            val mReadEXTERNAL = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
            val mWriteEXTERNAL = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (mReadEXTERNAL != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(atividade, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), readInternalExternalStorageRequestCode)
            } else {
                @Suppress("DEPRECATION")
                folder = File(Environment.getExternalStorageDirectory(), appFolder)
                if (!folder.exists()) {
                    folder.mkdir()
                }
                readInternalExternalStorage = true
            }
            if (mWriteEXTERNAL != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(atividade, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), writeInternalExternalStorageRequestCode)
            } else {
                @Suppress("DEPRECATION")
                folder = File(Environment.getExternalStorageDirectory(), appFolder)
                if (!folder.exists()) {
                    folder.mkdir()
                }
                readInternalExternalStorage = true
            }
        }
        return readInternalExternalStorage
    }



}

