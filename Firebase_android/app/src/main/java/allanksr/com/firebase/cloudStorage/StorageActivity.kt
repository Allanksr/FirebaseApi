package allanksr.com.firebase.cloudStorage

import allanksr.com.firebase.R
import allanksr.com.firebase.permissions.Permissions
import allanksr.com.firebase.cloudStorage.adapter.GetterSetterRecycler
import allanksr.com.firebase.cloudStorage.adapter.RecyclerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.storage_activity.*


class StorageActivity: AppCompatActivity(), StorageView, RecyclerAdapter.ItemClickListener {
    private lateinit var storageViewModel: StorageViewModel

    private var horizontalLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private lateinit var pairs: MutableList<GetterSetterRecycler>
    private var recyclerAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.storage_activity)

        storageViewModel = ViewModelProvider(
                this,
                StorageViewModel.StorageViewModelFactory(StorageRepository(this))
        ).get(StorageViewModel::class.java)


        tv_file.setOnClickListener{
            getFile()
        }

        pairs = ArrayList()
        recycler_Storage?.layoutManager = horizontalLayoutManager
        storageViewModel.listFiles()
    }

    private val startGaleryImage = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        val originalUri = result.data?.data
        tv_file.text = originalUri.toString()
        bt_updload.visibility = View.VISIBLE
        bt_updload.setOnClickListener{
            try {
                bt_updload.visibility = View.GONE
                storageViewModel.uploadFile(this, originalUri!!)
            } catch (e: Exception) {
                Log.d("Exception","${e.message}")
            }
        }
    }

    private fun getFile(){
        if(permission()){
            val i1 = Intent(Intent.ACTION_OPEN_DOCUMENT)
            i1.addCategory(Intent.CATEGORY_OPENABLE)
            i1.type = "*/*"
            val extraMimeTypes = arrayOf(
                "video/*",
                "audio/*",
                "image/*",
                "application/vnd.android.package-archive"
            )
            i1.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes)
            startGaleryImage.launch(i1)

        }else{
            Log.d(componentName.className, "${permission()}")
        }
    }

    private fun permission(): Boolean{
        return Permissions().readwriteStorage(this, this)
    }

    override fun waitResult() {
        include_loading.isClickable = true
        include_loading.visibility = View.VISIBLE
        la_loading.playAnimation()
        include_loading.setOnClickListener{
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
        }
    }

    override fun setError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        tv_file.text = error
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }


    override fun setProgress(progress: String) {
        tv_file.text = progress
    }

    override fun getData(fileName: String, fileUrl: String) {
        recycler_Storage.visibility = View.VISIBLE
        pairs.add(GetterSetterRecycler(
            fileName,
            fileUrl
              )
           )
        recyclerAdapter = RecyclerAdapter(applicationContext, pairs)
        recyclerAdapter?.setClickListener(this)
        recyclerAdapter?.notifyDataSetChanged()
        recycler_Storage?.adapter = recyclerAdapter
    }

    override fun addData(fileName: String, fileUrl: String) {
        recycler_Storage.visibility = View.VISIBLE
        pairs.add(GetterSetterRecycler(
                fileName,
                fileUrl
        )
        )
        recyclerAdapter = RecyclerAdapter(applicationContext, pairs)
        recyclerAdapter?.setClickListener(this)
        recyclerAdapter?.notifyDataSetChanged()
        recycler_Storage?.adapter = recyclerAdapter
        include_loading.visibility = View.GONE
        la_loading.cancelAnimation()
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_LONG).show()

        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.alertDialog)
        builder.setTitle(pairs[position].fileName)
        builder.setIcon(R.drawable.firebase)
        builder.setMessage(pairs[position].fileUrl)
                .setCancelable(true)
                .setPositiveButton("Delete") { _, _ ->
                    storageViewModel.deleteFile(pairs[position].fileName)

                    pairs.find {
                        pairs[position].fileName == it.fileName
                    }.let {
                        pairs.remove(it)
                        recyclerAdapter = RecyclerAdapter(this, pairs)
                        recyclerAdapter?.setClickListener(this)
                        recyclerAdapter?.notifyDataSetChanged()
                        recycler_Storage?.adapter = recyclerAdapter
                    }

                }
                .setNegativeButton("View") { _, _ ->
                    val fileUrl = Uri.parse(pairs[position].fileUrl)
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, fileUrl))
                    } catch (e: Exception) {
                        Toast.makeText(this, "install a browser", Toast.LENGTH_LONG).show()
                    }
                }

        val alertDialog = builder.create()
        alertDialog.show()

    }

}
