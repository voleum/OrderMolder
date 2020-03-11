package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.HashMap;

import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.ui.orders.GoodsChooserActivity;

public class GoodsChooserItemViewModel extends BaseObservable {

    private DecimalFormat df;

    private HashMap<String, Object> values;
    private Good good;

    public GoodsChooserItemViewModel(HashMap<String, Object> values) {
        this.values = values;
        this.good = (Good) values.get(GoodsChooserActivity.GOOD);
        df = DecimalHelper.newMoneyFieldFormat();
    }

    @Bindable
    public String getName() {
        return good.getName();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(df.format(values.get(GoodsChooserActivity.PRICE)));
    }
}
