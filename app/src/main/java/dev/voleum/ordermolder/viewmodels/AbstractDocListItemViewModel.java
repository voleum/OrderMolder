package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Locale;

import dev.voleum.ordermolder.models.Table;

public abstract class AbstractDocListItemViewModel<T extends Table, E extends AbstractDocViewModel> extends BaseObservable {

    protected DecimalFormat df;

    protected T table;
    protected E viewModel;

    public AbstractDocListItemViewModel(T table, E viewModel) {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        df = new DecimalFormat(format, otherSymbols);

        this.table = table;
        this.viewModel = viewModel;
    }

    @Bindable
    public String getName() {
        return table.getObjName();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(table.getSum());
    }
}
