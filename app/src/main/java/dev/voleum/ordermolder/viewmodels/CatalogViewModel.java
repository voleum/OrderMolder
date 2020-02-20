package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.objects.EconomicEntity;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Unit;

public class CatalogViewModel<T extends Catalog> extends BaseObservable {

    private T catalog;
    private String name;
    private String tin;
    private String group;
    private String unit;
    private String fullName;
    private String code;

    public CatalogViewModel(T catalog) {
        this.catalog = catalog;
        this.name = catalog.getName();
        this.tin = catalog instanceof EconomicEntity ? ((EconomicEntity) catalog).getTin() : null;
        this.group = catalog instanceof Good ? ((Good) catalog).getGroupName() : null;
        this.unit = catalog instanceof Good ? ((Good) catalog).getUnitName() : null;
        this.fullName = catalog instanceof Unit ? ((Unit) catalog).getFullName() : null;
        this.code = catalog instanceof Unit ? String.valueOf(((Unit) catalog).getCode()) : null;
    }

    @Bindable
    public T getCatalog() {
        return catalog;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getTin() {
        return tin;
    }

    @Bindable
    public String getGroup() {
        return group;
    }

    @Bindable
    public String getUnit() {
        return unit;
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }

    @Bindable
    public String getCode() {
        return code;
    }
}
