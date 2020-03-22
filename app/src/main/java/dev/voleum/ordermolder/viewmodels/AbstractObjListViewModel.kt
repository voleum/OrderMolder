package dev.voleum.ordermolder.viewmodels

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.models.Obj

abstract class AbstractObjListViewModel<T : Obj, E : RecyclerView.Adapter<*>> : BaseObservable() {

    open lateinit var objs: List<T>
        @Bindable get

    open lateinit var adapter: E
        @Bindable set
        @Bindable get

    protected abstract fun initList()
}