package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.HashMap;

import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.ui.orders.GoodsChooserActivity;

public class GoodsChooserItemViewModel extends BaseObservable {

    private HashMap<String, Object> values;
    private Good good;

    public GoodsChooserItemViewModel(HashMap<String, Object> values) {
        this.values = values;
        this.good = (Good) values.get(GoodsChooserActivity.GOOD);
    }

    @Bindable
    public String getName() {
        return good.getName();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(values.get(GoodsChooserActivity.PRICE));
    }
}
