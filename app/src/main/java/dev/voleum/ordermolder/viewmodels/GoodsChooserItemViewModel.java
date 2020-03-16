package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.models.Price;

public class GoodsChooserItemViewModel extends BaseObservable {

    private DecimalFormat df;

    private Price price;

    public GoodsChooserItemViewModel(Price values) {
        this.price = values;
        df = DecimalHelper.newMoneyFieldFormat();
    }

    @Bindable
    public String getName() {
        return price.getGoodName();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(df.format(price.getPrice()));
    }
}
