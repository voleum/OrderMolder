package dev.voleum.ordermolder.viewmodels

import androidx.databinding.Bindable
import dev.voleum.ordermolder.models.Order

class ObjectsChooserItemViewModel(order: Order) : AbstractChooserItemViewModel<Order>(order) {

    val sum: String
        @Bindable get() = df.format(item.sum)
}