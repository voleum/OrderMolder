package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.HashMap;

import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.ui.cashreceipts.ObjectsChooserActivity;

public class ObjectsChooserItemViewModel extends BaseObservable {

    private DecimalFormat df;

    private HashMap<String, Object> values;
    private Order object;

    public ObjectsChooserItemViewModel(HashMap<String, Object> values) {
        this.values = values;
        this.object = (Order) values.get(ObjectsChooserActivity.OBJECT);
        df = DecimalHelper.newMoneyFieldFormat();
    }

    @Bindable
    public String getName() {
        return object.toString();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(values.get(ObjectsChooserActivity.SUM)));
    }
}
