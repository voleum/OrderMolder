package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.Order;

public class ObjectsChooserItemViewModel extends AbstractChooserItemViewModel {

    private Order order;

    public ObjectsChooserItemViewModel(Order order) {
        super();
        this.order = order;
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
