package ben.upsilon.up.done

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ben.upsilon.up.R

/**
 *
 * Created by ben on 2/19/16.
 */

data class DoneItem(val id: String, val content: String)

class DoneItemAdapter(val items: List<DoneItem>, val mListener: OnListInteractionListener?) : RecyclerView.Adapter<DoneItemAdapter.ViewHolder>() {




    override fun getItemCount(): Int {
        Log.d("ben.upsilon", items.toString())
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mContentView.text = items[position].content

        holder.mView.setOnClickListener {
            mListener?.onItem(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_done, parent, false)
        return ViewHolder(view)
    }

    interface OnListInteractionListener {
        fun onItem(mItem: DoneItem)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mView = itemView
        //        var mIdView: TextView = itemView.findViewById(R.id.id) as TextView
        val mContentView: TextView = itemView.findViewById(R.id.content) as TextView
        //        val mItem: DoneItem


    }
}