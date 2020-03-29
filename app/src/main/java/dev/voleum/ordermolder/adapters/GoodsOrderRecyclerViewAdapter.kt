package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.OrderGoodHolderBinding
import dev.voleum.ordermolder.models.TableGoods
import dev.voleum.ordermolder.viewmodels.GoodsOrderListItemViewModel
import dev.voleum.ordermolder.viewmodels.OrderViewModel

class GoodsOrderRecyclerViewAdapter(list: List<TableGoods>, val viewModel: OrderViewModel) : AbstractRecyclerViewAdapter<TableGoods>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: OrderGoodHolderBinding = DataBindingUtil
                .inflate(LayoutInflater
                        .from(parent.context),
                        R.layout.order_good_holder,
                        parent,
                        false)
        return GoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as GoodViewHolder)
                .binding as OrderGoodHolderBinding)
                .row = GoodsOrderListItemViewModel(list[position], viewModel)
    }

    inner class GoodViewHolder(binding: OrderGoodHolderBinding) : AbstractViewHolder(binding)
}