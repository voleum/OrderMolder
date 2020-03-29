package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.DocHolderBinding
import dev.voleum.ordermolder.models.Document
import dev.voleum.ordermolder.viewmodels.ObjListItemViewModel

class DocListRecyclerViewAdapter<T : Document<*>>(list: List<T>) : AbstractRecyclerViewAdapter<T>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: DocHolderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.doc_holder,
                        parent,
                        false)
        return DocViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as DocListRecyclerViewAdapter<*>.DocViewHolder)
                .binding as DocHolderBinding)
                .row = ObjListItemViewModel(list[position])
    }

    inner class DocViewHolder(binding: DocHolderBinding) : AbstractViewHolder(binding) {

        init {
            binding.root.setOnClickListener { v -> onEntryClickListener.onEntryClick(v, layoutPosition) }
        }
    }
}