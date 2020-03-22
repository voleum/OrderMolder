package dev.voleum.ordermolder.viewmodels

import androidx.databinding.Bindable
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.models.Obj

abstract class AbstractChooserViewModel<T : Obj, E : RecyclerView.Adapter<*>> : ViewModelObservable() {

    open var items: List<T>? = null
        @Bindable get

    open lateinit var adapter: E
        @Bindable set
        @Bindable get
}