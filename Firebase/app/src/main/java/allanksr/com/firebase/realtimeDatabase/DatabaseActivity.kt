package allanksr.com.firebase.realtimeDatabase

import allanksr.com.firebase.R
import allanksr.com.firebase.realtimeDatabase.adapter.GetterSetterRecycler
import allanksr.com.firebase.realtimeDatabase.adapter.RecyclerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.loading.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.database_activity.*
import kotlinx.android.synthetic.main.edit_data.*

class DatabaseActivity: AppCompatActivity(), RecyclerAdapter.ItemClickListener, DatabaseView {
    private var childEventListener: ChildEventListener? =null
    private lateinit var referenceChildEvent : DatabaseReference
    private lateinit var databaseViewModel: DatabaseViewModel
    private var horizontalLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private lateinit var pairs: MutableList<GetterSetterRecycler>
    private var recyclerAdapter: RecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_activity)

        databaseViewModel = ViewModelProvider(
            this,
                DatabaseViewModel.DatabaseViewModelFactory(DatabaseRepository(this))
        ).get(DatabaseViewModel::class.java)

        pairs = ArrayList()
        recycler_Database?.layoutManager = horizontalLayoutManager
        childEvent()

        bt_save.setOnClickListener{
            val data = DataSet("${edt_name.text}", "${edt_email.text}", "${edt_image.text}")
            databaseViewModel.addUser(data)
        }

    }

    private fun childEvent(){
        referenceChildEvent =  databaseViewModel.databaseGetInstance().child("users/")

        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(componentName.className,"onChildAdded :${snapshot.key}")

                pairs.add(GetterSetterRecycler(
                        pairs.size,
                        "${snapshot.key}",
                        "${snapshot.getValue(DataGet::class.java)?.name}",
                        "${snapshot.getValue(DataGet::class.java)?.email}",
                        "${snapshot.getValue(DataGet::class.java)?.imageUrl}")
                   )

                recyclerAdapter = RecyclerAdapter(applicationContext, pairs)
                recyclerAdapter?.setClickListener(this@DatabaseActivity)
                recyclerAdapter?.notifyDataSetChanged()
                recycler_Database?.adapter = recyclerAdapter
                recycler_Database?.smoothScrollToPosition(pairs.size - 1)

                include_edit_data.visibility = View.GONE
                include_loading.visibility = View.GONE
                la_loading.cancelAnimation()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(componentName.className,"onChildChanged :${snapshot.key}")
                pairs.find {
                    snapshot.key == it.documentId
                }.let {

                    pairs[it!!.position] = GetterSetterRecycler(
                            it.position,
                            "${snapshot.key}",
                            "${snapshot.getValue(DataGet::class.java)?.name}",
                            "${snapshot.getValue(DataGet::class.java)?.email}",
                            "${snapshot.getValue(DataGet::class.java)?.imageUrl}"
                    )

                    recyclerAdapter = RecyclerAdapter(this@DatabaseActivity, pairs)
                    recyclerAdapter?.setClickListener(this@DatabaseActivity)
                    recyclerAdapter?.notifyDataSetChanged()
                    recycler_Database?.adapter = recyclerAdapter
                    recycler_Database?.smoothScrollToPosition(it.position)
                    include_edit_data.visibility = View.GONE
                    include_loading.visibility = View.GONE
                    la_loading.cancelAnimation()
                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(componentName.className,"onChildRemoved :${snapshot.key}")
                pairs.find {
                    snapshot.key == it.documentId
                }.let {
                    pairs.remove(it)
                    recyclerAdapter = RecyclerAdapter(this@DatabaseActivity, pairs)
                    recyclerAdapter?.setClickListener(this@DatabaseActivity)
                    recyclerAdapter?.notifyDataSetChanged()
                    recycler_Database?.adapter = recyclerAdapter
                }

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(componentName.className,"onChildMoved :${snapshot.key}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(componentName.className,"onCancelled :${error.toException()}")

            }
        }
        referenceChildEvent.addChildEventListener(childEventListener!!)
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
                        databaseViewModel.updateDoc(
                                pairs[position].documentId,
                                DataSet("${edt_update_name.text}",
                                        "${edt_update_email.text}",
                                        "${edt_update_image.text}"
                                )
                        )
                    }

                    bt_cancel.setOnClickListener{
                        include_edit_data.visibility = View.GONE
                    }

                }
                .setNegativeButton("Delete") { _, _ ->
                    databaseViewModel.deleteDoc(pairs[position].documentId)
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
        tv_Firebase_Database.visibility = View.GONE
    }

    public override fun onDestroy() {
        super.onDestroy()
        referenceChildEvent.removeEventListener(childEventListener!!)
    }

}
