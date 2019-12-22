package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.HashMap;

import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.ui.cashreceipts.ObjectsChooserActivity;

public class ObjectsChooserItemViewModel extends BaseObservable {

    private HashMap<String, Object> values;
    private Order object;

    public ObjectsChooserItemViewModel(HashMap<String, Object> values) {
        this.values = values;
        this.object = (Order) values.get(ObjectsChooserActivity.OBJECT);
    }

    @Bindable
    public String getName() {
        return object.toString();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(values.get(ObjectsChooserActivity.SUM));
    }
}
