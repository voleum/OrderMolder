package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Obj;

abstract public class AbstractChooserViewModel<T extends Obj, E extends RecyclerView.Adapter> extends ViewModelObservable {

    protected List<T> items;
    protected E adapter;

    @Bindable
    public void setAdapter(E adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public E getAdapter() {
        return adapter;
    }

    @Bindable
    public List<T> getItems() {
        return items;
    }
}
