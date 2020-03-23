package dev.voleum.ordermolder.viewmodels

import androidx.databinding.Bindable
import dev.voleum.ordermolder.models.Price

class GoodsChooserItemViewModel(price: Price) : AbstractChooserItemViewModel<Price>(price) {

    val price: String
        @Bindable get() = df.format(item.price)
}