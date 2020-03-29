package dev.voleum.ordermolder.viewmodels

import android.icu.math.BigDecimal
import android.widget.AdapterView
import androidx.databinding.Bindable
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.ordermolder.BR
import dev.voleum.ordermolder.OrderMolder
import dev.voleum.ordermolder.database.DbRoom
import dev.voleum.ordermolder.helpers.DecimalHelper
import dev.voleum.ordermolder.helpers.TableViewModelItemTouchHelper
import dev.voleum.ordermolder.models.Company
import dev.voleum.ordermolder.models.Document
import dev.voleum.ordermolder.models.Partner
import dev.voleum.ordermolder.models.Table
import io.reactivex.Completable
import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractDocViewModel<D : Document<T>, T : Table, E : RecyclerView.Adapter<*>> : ViewModelObservable(), AdapterView.OnItemSelectedListener, TableViewModelItemTouchHelper {

    private val df = DecimalHelper.moneyFieldFormat()

    var document: D? = null
        @Bindable get
        @Bindable set

    open var table: MutableList<T>? = null
        @Bindable get

    open var adapter: E? = null
        @Bindable set
        @Bindable get

    open var title: String
        @Bindable set(value) { document.toString() }
        @Bindable get() = document.toString()

    open var date: String?
        @Bindable set(value) { document?.date = value }
        @Bindable get() = document?.date

    open var time: String?
        @Bindable set(value) { document?.time = value }
        @Bindable get() = document?.time

    open var sum: String?
        @Bindable set(value) { document?.sum = value!!.toDouble() }
        @Bindable get() = if (document != null) df.format(document!!.sum) else "0.00"

    open var companies: MutableList<Company>? = null
        @Bindable get

    open var partners: MutableList<Partner>? = null
        @Bindable get

    open var selectedItemCompany = 0
        @Bindable set
        @Bindable get

    open var selectedItemPartner = 0
        @Bindable set
        @Bindable get

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        table?.swap(fromPosition, toPosition)
        adapter!!.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        table!!.removeAt(position)
        adapter!!.notifyItemRemoved(position)
        countSum()
    }

    open fun checkUid() {
        if (document!!.uid.isEmpty()) {
            val uid = UUID.randomUUID().toString()
            document!!.uid = uid
            table!!.forEach { it.uid = uid }
        }
    }

    open fun countSum() {
        val sum = BigDecimal.valueOf(table!!.sumByDouble { it.sum })
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .toDouble()
        document?.sum = sum
        notifyPropertyChanged(BR.sum)
    }

    open fun initSpinnersData(): Completable {

        return Completable.create {

            val db = OrderMolder.getApplication().database
            initCompanies(db)
            initPartners(db)
            initOthers(db)
            it.onComplete()
        }
    }

    private fun initCompanies(db: DbRoom) {
        companies = ArrayList()
        val iterator = db.companyDao.all.listIterator()
        while (iterator.hasNext()) {
            iterator.next().let {
                companies!!.add(it)
                if (it.uid == document!!.companyUid) {
                    selectedItemCompany = iterator.previousIndex()
                    notifyPropertyChanged(BR.companies)
                }
            }
        }
    }

    private fun initPartners(db: DbRoom) {
        partners = ArrayList()
        val iterator = db.partnerDao.all.listIterator()
        while (iterator.hasNext()) {
            iterator.next().let {
                partners!!.add(it)
                if (it.uid == document!!.partnerUid) {
                    selectedItemPartner = iterator.previousIndex()
                    notifyPropertyChanged(BR.partners)
                }
            }
        }
    }

    protected open fun initOthers(db: DbRoom) {}

    fun numberTable() {
        table!!.forEach { it.position = table!!.indexOf(it) }
    }

    abstract fun getDocByUid(uid: String): Completable

    abstract fun saveDoc(document: D): Completable
}

private fun <E> MutableList<E>?.swap(index1: Int, index2: Int) {
    val tmp: E = this!![index1]
    this[index1] = this[index2]
    this[index2] = tmp
}
