package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

import dev.voleum.ordermolder.models.Catalog;
import dev.voleum.ordermolder.models.EconomicEntity;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.models.Unit;

public class CatalogViewModel<T extends Catalog> extends ViewModelObservable implements Observable {

    private T catalog;
    private String name;
    private String tin;
    private String group;
    private String unit;
    private String fullName;
    private String code;

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

    public void setCatalog(T catalog) {
        if (this.catalog != null) return;
        this.catalog = catalog;
        this.name = catalog.getName();
        this.tin = catalog instanceof EconomicEntity ? ((EconomicEntity) catalog).getTin() : null;
        this.group = catalog instanceof Good ? ((Good) catalog).getGroupName() : null;
        this.unit = catalog instanceof Good ? ((Good) catalog).getUnitName() : null;
        this.fullName = catalog instanceof Unit ? ((Unit) catalog).getFullName() : null;
        this.code = catalog instanceof Unit ? String.valueOf(((Unit) catalog).getCode()) : null;
    }
}
