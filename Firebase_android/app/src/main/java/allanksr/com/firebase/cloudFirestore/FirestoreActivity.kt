package allanksr.com.firebase.cloudFirestore

import allanksr.com.firebase.Prefs
import allanksr.com.firebase.R
import allanksr.com.firebase.cloudFirestore.adapter.GetterSetterRecycler
import allanksr.com.firebase.cloudFirestore.adapter.RecyclerAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.firestore_activity.*
import kotlinx.android.synthetic.main.loading.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.edit_data.*

class FirestoreActivity: AppCompatActivity(), RecyclerAdapter.ItemClickListener, FireStoreView {
    private lateinit var mFireStoreViewModel: FireStoreViewModel
    private var horizontalLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private lateinit var pairs: MutableList<GetterSetterRecycler>
    private var recyclerAdapter: RecyclerAdapter? = null
    private var preferences = Prefs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firestore_activity)

        mFireStoreViewModel = ViewModelProvider(
            this,
            FireStoreViewModel.FireStoreViewModelFactory(FireStoreRepository(this))
        ).get(FireStoreViewModel::class.java)

        pairs = ArrayList()
        recycler_FireStore?.layoutManager = horizontalLayoutManager

        mFireStoreViewModel.fireStoreData.observe(this, {res->
            res.addOnCompleteListener {
                pairs.add(GetterSetterRecycler(res.result.id, res.result.name, res.result.email, res.result.imageUrl))
                recyclerAdapter =  RecyclerAdapter(this, pairs)
                recyclerAdapter?.setClickListener(this)
                recyclerAdapter?.notifyDataSetChanged()
                recycler_FireStore?.adapter = recyclerAdapter
                recycler_FireStore?.smoothScrollToPosition(pairs.size - 1)
                preferences.setStringSP(this, "docId", res.result.id)

                include_edit_data.visibility = View.GONE
                include_loading.visibility = View.GONE
                la_loading.cancelAnimation()
            }

        })

        bt_save.setOnClickListener{
            val data = DataSet("${edt_name.text}", "${edt_email.text}", "${edt_image.text}")
            mFireStoreViewModel.addUser(data)
        }

    }

    override fun onItemClick(view: View, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.alertDialog)
        builder.setTitle(pairs[position].name)
        builder.setIcon(R.drawable.firebase)
        builder.setMessage(pairs[position].email)
                .setCancelable(true)
                .setPositiveButton("Edit") { _, _ ->
                    include_edit_data.isClickable = true
                    include_edit_data.visibility = View.VISIBLE
                    bt_update.setOnClickListener{
                        mFireStoreViewModel.updateDoc(
                                pairs[position].documentId,
                                DataSet("${edt_update_name.text}",
                                        "${edt_update_email.text}",
                                        "${edt_update_image.text}"
                                )
                        )

                        pairs.remove(pairs[position])
                        recyclerAdapter =  RecyclerAdapter(this, pairs)
                        recyclerAdapter?.setClickListener(this)
                        recyclerAdapter?.notifyDataSetChanged()
                        recycler_FireStore?.adapter = recyclerAdapter
                    }
                    bt_cancel.setOnClickListener{
                        include_edit_data.visibility = View.GONE
                    }

                }
                .setNegativeButton("Delete") { _, _ ->
                    preferences.setStringSP(this, "docId", "")
                    mFireStoreViewModel.deleteDoc(pairs[position].documentId)
                    Toast.makeText(this, "deleting ${pairs[position].documentId}", Toast.LENGTH_SHORT).show()
                    pairs.remove(pairs[position])
                    recyclerAdapter =  RecyclerAdapter(this, pairs)
                    recyclerAdapter?.setClickListener(this)
                    recyclerAdapter?.notifyDataSetChanged()
                    recycler_FireStore?.adapter = recyclerAdapter
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
        val alertDialog = builder.create()
        alertDialog.show()


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
    }

    override fun getData() {
        tv_Firebase_FireStore.visibility = View.GONE
        mFireStoreViewModel.getDoc()
    }

    public override fun onStart() {
        super.onStart()
        mFireStoreViewModel.getDocSaved(this)
    }

}
