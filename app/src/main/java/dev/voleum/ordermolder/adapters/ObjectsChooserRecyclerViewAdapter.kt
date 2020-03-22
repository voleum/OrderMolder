package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.ChooserObjectHolderBinding
import dev.voleum.ordermolder.models.Order
import dev.voleum.ordermolder.viewmodels.ObjectsChooserItemViewModel

class ObjectsChooserRecyclerViewAdapter(list: List<Order>) : AbstractRecyclerViewAdapter<Order>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ChooserObjectHolderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                R.layout.chooser_object_holder,
                parent,
                false)
        return ObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as ObjectViewHolder)
                .binding as ChooserObjectHolderBinding)
                .row = ObjectsChooserItemViewModel(list[position])
    }

    inner class ObjectViewHolder(binding: ChooserObjectHolderBinding) : AbstractViewHolder(binding) {

        init {
            binding.root.setOnClickListener { v -> onEntryClickListener.onEntryClick(v, layoutPosition) }
        }
    }
}