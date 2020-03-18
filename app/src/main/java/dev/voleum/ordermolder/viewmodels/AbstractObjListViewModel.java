package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.models.Obj;

public abstract class AbstractObjListViewModel<T extends Obj, E extends RecyclerView.Adapter> extends BaseObservable {

    protected List<? extends T> objs;
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
    public List<? extends T> getObjs() {
        return objs;
    }

    protected abstract void initList();
}
