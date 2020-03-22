package dev.voleum.ordermolder.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.models.Obj

abstract class AbstractRecyclerViewAdapter<T : Obj>(var list: List<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onEntryClickListener: OnEntryClickListener
    lateinit var onEntryLongClickListener: OnEntryLongClickListener

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    abstract inner class AbstractViewHolder(open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    interface OnEntryClickListener {
        fun onEntryClick(v: View?, position: Int)
    }

    interface OnEntryLongClickListener {
        fun onEntryLongClick(v: View, position: Int): Boolean
    }
}