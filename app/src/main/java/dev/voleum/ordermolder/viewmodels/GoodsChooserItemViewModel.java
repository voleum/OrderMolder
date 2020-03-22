package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.Price;

public class GoodsChooserItemViewModel extends AbstractChooserItemViewModel {

    private Price price;

    public GoodsChooserItemViewModel(Price price) {
        super();
        this.price = price;
    }

    @Bindable
    public String getName() {
        return price.getGoodName();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(getDf().format(price.getPrice()));
    }
}
