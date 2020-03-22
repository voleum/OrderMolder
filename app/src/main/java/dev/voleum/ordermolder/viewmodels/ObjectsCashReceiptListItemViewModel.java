package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.TableObjects;

public class ObjectsCashReceiptListItemViewModel extends AbstractDocListItemViewModel<TableObjects, CashReceiptViewModel> {

    public ObjectsCashReceiptListItemViewModel(TableObjects table, CashReceiptViewModel viewModel) {
        super(table, viewModel);
    }

    @Bindable
    public String getObjectName() {
        return getTable().getObjName();
    }

    @Bindable
    public void setSum(String sum) {
        getTable().setSum(Double.parseDouble(sum));
        getViewModel().countSum();
    }
}
