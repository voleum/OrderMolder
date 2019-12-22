package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.objects.Catalog;

public class CatalogListItemViewModel extends BaseObservable {

    private Catalog catalog;

    public CatalogListItemViewModel(Catalog catalog) {
        this.catalog = catalog;
    }

    @Bindable
    public String getCatalogTitle() {
        return catalog.toString();
    }
}
