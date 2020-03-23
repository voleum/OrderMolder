package dev.voleum.ordermolder.viewmodels

import androidx.databinding.Bindable
import androidx.databinding.Observable
import dev.voleum.ordermolder.models.Catalog
import dev.voleum.ordermolder.models.EconomicEntity
import dev.voleum.ordermolder.models.Good
import dev.voleum.ordermolder.models.Unit

class CatalogViewModel<T : Catalog> : ViewModelObservable(), Observable {

    var catalog: T? = null
        @Bindable get
        set(value) {
            if (field != null) return
            field = value
            name = catalog!!.name
            if (catalog is EconomicEntity) tin = (catalog!! as EconomicEntity).tin
            if (catalog is Good) {
                group = (catalog!! as Good).groupName
                unit = (catalog!! as Good).unitName
            }
            if (catalog is Unit) {
                fullName = (catalog!! as Unit).fullName
                code = (catalog!! as Unit).code.toString()
            }
        }

    var name: String? = null
        @Bindable get

    var tin: String? = null
        @Bindable get

    var group: String? = null
        @Bindable get

    var unit: String? = null
        @Bindable get

    var fullName: String? = null
        @Bindable get

    var code: String? = null
        @Bindable get
}