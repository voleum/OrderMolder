package dev.voleum.ordermolder.viewmodels

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.voleum.ordermolder.helpers.DecimalHelper
import dev.voleum.ordermolder.models.Table

abstract class AbstractDocListItemViewModel<T : Table, E : AbstractDocViewModel<*, *, *>>(var table: T, var viewModel: E) : BaseObservable() {

    open val df = DecimalHelper.moneyFieldFormat()

    open var name: String = table.objName
        @Bindable get

    open var sum: String = table.sum.toString()
        @Bindable get
}