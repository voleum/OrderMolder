package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.Catalog;

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
