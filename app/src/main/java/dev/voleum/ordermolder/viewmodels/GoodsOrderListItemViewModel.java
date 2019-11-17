package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Locale;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.objects.TableGoods;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GoodsOrderListItemViewModel extends BaseObservable implements View.OnClickListener {

    private DecimalFormat df;

    private TableGoods tableGoods;
    private OrderViewModel orderViewModel;

    public GoodsOrderListItemViewModel(TableGoods tableGoods, OrderViewModel orderViewModel) {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        df = new DecimalFormat(format, otherSymbols);

        this.tableGoods = tableGoods;
        this.orderViewModel = orderViewModel;
    }

    @Bindable
    public String getGoodName() {
        return tableGoods.getGoodName();
    }

    @Bindable
    public void setQuantity(String quantity) {
        double q = 0.0;
        try {
            q = Double.parseDouble(quantity);
        } catch (NumberFormatException ignored) {

        }
        tableGoods.setQuantity(q);
        tableGoods.countSum();
        orderViewModel.countSum();
    }

    @Bindable
    public String getQuantity() {
        return String.valueOf(df.format(tableGoods.getQuantity()));
    }

    @Bindable
    public void setPrice(String price) {
        double p = 0.0;
        try {
            p = Double.parseDouble(price);
        } catch (NumberFormatException ignored) {

        }
        tableGoods.setPrice(p);
        tableGoods.countSum();
        orderViewModel.countSum();
    }

    @Bindable
    public String getPrice() {
        return String.valueOf(df.format(tableGoods.getPrice()));
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
