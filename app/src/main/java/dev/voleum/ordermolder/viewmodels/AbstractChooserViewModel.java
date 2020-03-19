package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.models.Obj;

public abstract class AbstractChooserViewModel<T extends Obj, E extends RecyclerView.Adapter> extends ViewModelObservable {

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
