package dev.voleum.ordermolder.viewmodels

import androidx.databinding.BaseObservable
import dev.voleum.ordermolder.helpers.DecimalHelper

abstract class AbstractChooserItemViewModel : BaseObservable() {

    protected val df = DecimalHelper.moneyFieldFormat()
}