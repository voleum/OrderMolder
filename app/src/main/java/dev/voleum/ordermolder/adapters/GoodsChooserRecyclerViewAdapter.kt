package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.ChooserGoodHolderBinding
import dev.voleum.ordermolder.models.Price
import dev.voleum.ordermolder.viewmodels.GoodsChooserItemViewModel

class GoodsChooserRecyclerViewAdapter(list: List<Price>) : AbstractRecyclerViewAdapter<Price>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ChooserGoodHolderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                        R.layout.chooser_good_holder,
                        parent,
                        false)
        return GoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as GoodViewHolder)
                .binding as ChooserGoodHolderBinding)
                .row = GoodsChooserItemViewModel(list[position])
    }

    inner class GoodViewHolder(binding: ChooserGoodHolderBinding) : AbstractViewHolder(binding) {

        init {
            binding.root.setOnClickListener { v -> onEntryClickListener.onEntryClick(v, layoutPosition) }
        }
    }
}