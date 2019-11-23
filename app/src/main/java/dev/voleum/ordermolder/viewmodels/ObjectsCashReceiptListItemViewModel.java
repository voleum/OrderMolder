package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Locale;

import dev.voleum.ordermolder.objects.TableObjects;

public class ObjectsCashReceiptListItemViewModel extends BaseObservable {

    private DecimalFormat df;

    private TableObjects tableObjects;
    private CashReceiptViewModel cashReceiptViewModel;

    public ObjectsCashReceiptListItemViewModel(TableObjects tableObjects, CashReceiptViewModel cashReceiptViewModel) {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        df = new DecimalFormat(format, otherSymbols);

        this.tableObjects = tableObjects;
        this.cashReceiptViewModel = cashReceiptViewModel;
    }

    @Bindable
    public String getObjectName() {
        return tableObjects.getObjectName();
    }

    @Bindable
    public void setSum(String sum) {
        tableObjects.setSum(Double.parseDouble(sum));
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(tableObjects.getSum()));
    }
}
