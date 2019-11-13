package dev.voleum.ordermolder.viewmodels;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.objects.TableGoods;

public class GoodsOrderListItemViewModel extends BaseObservable  implements View.OnClickListener {

    private TableGoods tableGoods;
    private OrderViewModel orderViewModel;

    public GoodsOrderListItemViewModel(TableGoods tableGoods, OrderViewModel orderViewModel) {
        this.tableGoods = tableGoods;
        this.orderViewModel = orderViewModel;
    }

//    @Bindable
//    public void setTableGoods(TableGoods tableGoods) {
//        this.tableGoods = tableGoods;
//    }

    @Bindable
    public String getGoodName() {
        return tableGoods.getGoodNane();
    }

    @Bindable
    public void setQuantity(String quantity) {
        tableGoods.setQuantity(Double.parseDouble(quantity));
        tableGoods.countSum();
        orderViewModel.countSum();
    }

    @Bindable
    public String getQuantity() {
        return String.valueOf(tableGoods.getQuantity());
    }

    @Bindable
    public void setPrice(String price) {
        tableGoods.setPrice(Double.parseDouble(price));
        tableGoods.countSum();
        orderViewModel.countSum();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(tableGoods.getPrice());
    }

    @Bindable
    public double getSum() {
        return tableGoods.getSum();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.good_plus:
                tableGoods.increaseQuantity();
                break;
            case R.id.good_minus:
                tableGoods.decreaseQuantity();
                break;
        }
        notifyChange();
        orderViewModel.countSum();
    }
}
