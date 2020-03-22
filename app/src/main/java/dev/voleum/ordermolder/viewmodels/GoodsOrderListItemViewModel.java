package dev.voleum.ordermolder.viewmodels;

import android.view.View;

import androidx.databinding.Bindable;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.models.TableGoods;

public class GoodsOrderListItemViewModel extends AbstractDocListItemViewModel<TableGoods, OrderViewModel> implements View.OnClickListener {

    public GoodsOrderListItemViewModel(TableGoods table, OrderViewModel viewModel) {
        super(table, viewModel);
    }

    @Bindable
    public void setQuantity(String quantity) {
        double q = 0.0;
        try {
            q = Double.parseDouble(quantity);
        } catch (NumberFormatException ignored) {

        }
        getTable().setQuantity(q);
        getTable().countSum();
        getViewModel().countSum();
    }

    @Bindable
    public String getQuantity() {
        return String.valueOf(getDf().format(getTable().getQuantity()));
    }

    @Bindable
    public void setPrice(String price) {
        double p = 0.0;
        try {
            p = Double.parseDouble(price);
        } catch (NumberFormatException ignored) {

        }
        getTable().setPrice(p);
        getTable().countSum();
        getViewModel().countSum();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(getDf().format(getTable().getPrice()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.good_plus:
                getTable().increaseQuantity();
                break;
            case R.id.good_minus:
                getTable().decreaseQuantity();
                break;
        }
        notifyChange();
        getViewModel().countSum();
    }
}
