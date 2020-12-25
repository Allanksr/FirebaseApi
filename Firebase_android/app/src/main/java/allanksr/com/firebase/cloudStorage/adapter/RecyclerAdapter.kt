package allanksr.com.firebase.cloudStorage.adapter

import allanksr.com.firebase.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class RecyclerAdapter
internal constructor(context: Context, private val pairs: List<GetterSetterRecycler>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private val ctx = context
    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.recycler_adapter_storage, parent, false)

        return ViewHolder(view)
    }
    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileName.text = pairs[position].fileName
        holder.urlFile.text = pairs[position].fileUrl

    }
    // total number of rows
    override fun getItemCount(): Int {
        return pairs.size
    }
    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        internal var fileName: TextView = itemView.findViewById(R.id.tv_fileName)
        internal var urlFile: TextView = itemView.findViewById(R.id.tv_urlFile)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
            Log.d("ClickRecyclerView",""+getItem(adapterPosition) )
        }
    }
    // convenience method for getting data at click position
     fun getItem(id: Int): Int {
        return id
    }
    // allows clicks events to be caught
    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }
    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}
