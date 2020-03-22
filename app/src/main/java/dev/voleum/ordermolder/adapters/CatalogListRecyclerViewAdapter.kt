package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.CatalogHolderBinding
import dev.voleum.ordermolder.models.Catalog
import dev.voleum.ordermolder.viewmodels.ObjListItemViewModel

class CatalogListRecyclerViewAdapter<T : Catalog>(list: List<T>) : AbstractRecyclerViewAdapter<T>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: CatalogHolderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.catalog_holder,
                        parent,
                        false)
        return CatalogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as CatalogListRecyclerViewAdapter<*>.CatalogViewHolder)
                .binding as CatalogHolderBinding)
                .row = ObjListItemViewModel(list[position])
    }

    inner class CatalogViewHolder(binding: CatalogHolderBinding) : AbstractViewHolder(binding) {

        init {
            binding.root.setOnClickListener { v -> onEntryClickListener.onEntryClick(v, layoutPosition) }
        }
    }
}