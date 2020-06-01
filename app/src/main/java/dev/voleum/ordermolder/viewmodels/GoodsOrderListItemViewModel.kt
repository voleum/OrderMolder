package dev.voleum.ordermolder.viewmodels

import android.view.View
import androidx.databinding.Bindable
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.models.TableGoods

class GoodsOrderListItemViewModel(table: TableGoods, viewModel: OrderViewModel) : AbstractDocListItemViewModel<TableGoods, OrderViewModel>(table, viewModel), View.OnClickListener {

    var quantity: String = ""
        @Bindable set(value) {
            val q = if (value == "") 0.00 else value.toDouble()
            table.quantity = q
            table.countSum()
            viewModel.countSum()
            field = q.toString()
        }
        @Bindable get() = df.format(table.quantity)

    var price: String = ""
        @Bindable set(value) {
            val p = if (value == "") 0.00 else value.toDouble()
            table.price = p
            table.countSum()
            viewModel.countSum()
            field = p.toString()
        }
        @Bindable get() = df.format(table.price)

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_input_start_icon -> table.decreaseQuantity()
            R.id.text_input_end_icon -> table.increaseQuantity()
//            R.id.good_plus -> table.increaseQuantity()
//            R.id.good_minus -> table.decreaseQuantity()
        }
        notifyChange()
    }
}