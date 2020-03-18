package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;

import androidx.databinding.BaseObservable;

import dev.voleum.ordermolder.helpers.DecimalHelper;

public abstract class AbstractChooserItemViewModel extends BaseObservable {

    protected DecimalFormat df;

    protected AbstractChooserItemViewModel() {
        df = DecimalHelper.newMoneyFieldFormat();
    }
}
