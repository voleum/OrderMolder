package dev.voleum.ordermolder.adapters

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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

    abstract inner class AbstractViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        val onEditorActionListener = TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm: InputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                v.clearFocus()
                true
            } else false
        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(v: View?, position: Int)
    }

    interface OnEntryLongClickListener {
        fun onEntryLongClick(v: View, position: Int): Boolean
    }
}