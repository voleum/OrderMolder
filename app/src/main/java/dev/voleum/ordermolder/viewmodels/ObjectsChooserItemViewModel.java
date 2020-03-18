package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.models.Order;

public class ObjectsChooserItemViewModel extends BaseObservable {

    private DecimalFormat df;

    private Order order;

    public ObjectsChooserItemViewModel(Order order) {
        this.order = order;
        df = DecimalHelper.newMoneyFieldFormat();
    }

    @Bindable
    public String getName() {
        return order.toString();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(order.getSum()));
    }
}
