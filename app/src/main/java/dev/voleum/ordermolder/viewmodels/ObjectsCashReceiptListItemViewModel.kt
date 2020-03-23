package dev.voleum.ordermolder.viewmodels

import androidx.databinding.Bindable
import dev.voleum.ordermolder.models.TableObjects

class ObjectsCashReceiptListItemViewModel(table: TableObjects, viewModel: CashReceiptViewModel) : AbstractDocListItemViewModel<TableObjects, CashReceiptViewModel>(table, viewModel) {

    override var sum: String
        get() = super.sum
        @Bindable set(value) {
            table.sum = value.toDouble()
            viewModel.countSum()
        }
}