package dev.voleum.ordermolder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.CashReceiptObjectHolderBinding
import dev.voleum.ordermolder.models.TableObjects
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel
import dev.voleum.ordermolder.viewmodels.ObjectsCashReceiptListItemViewModel

class ObjectsCashReceiptRecyclerViewAdapter(list: List<TableObjects>, val viewModel: CashReceiptViewModel) : AbstractRecyclerViewAdapter<TableObjects>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: CashReceiptObjectHolderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context),
                R.layout.cash_receipt_object_holder,
                parent,
                false)
        return ObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ((holder as ObjectViewHolder)
                .binding as CashReceiptObjectHolderBinding)
                .row = ObjectsCashReceiptListItemViewModel(list[position], viewModel)
    }

    inner class ObjectViewHolder(binding: CashReceiptObjectHolderBinding) : AbstractViewHolder(binding)
}