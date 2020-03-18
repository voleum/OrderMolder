package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.Obj;

public class ObjListItemViewModel<T extends Obj> extends BaseObservable {

    private T obj;

    public ObjListItemViewModel(T obj) {
        this.obj = obj;
    }

    @Bindable
    public String getTitle() {
        return obj.toString();
    }
}
