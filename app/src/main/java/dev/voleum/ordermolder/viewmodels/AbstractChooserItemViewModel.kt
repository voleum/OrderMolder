package dev.voleum.ordermolder.viewmodels

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.voleum.ordermolder.helpers.DecimalHelper
import dev.voleum.ordermolder.models.Obj

abstract class AbstractChooserItemViewModel<T : Obj>(open val item: T) : BaseObservable() {

    protected val df = DecimalHelper.moneyFieldFormat()

    val name: String
        @Bindable get() = item.toString()
}