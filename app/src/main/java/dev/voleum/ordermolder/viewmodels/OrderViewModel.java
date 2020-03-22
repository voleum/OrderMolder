package dev.voleum.ordermolder.viewmodels;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import dev.voleum.ordermolder.BR;
import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.TableGoods;
import dev.voleum.ordermolder.models.Warehouse;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends AbstractDocViewModel<Order, TableGoods, GoodsOrderRecyclerViewAdapter> implements Spinner.OnItemSelectedListener {

    private List<Warehouse> warehouses;
    private int selectedItemWarehouse;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.order_spinner_companies:
                getDocument().setCompanyUid((getCompanies().get(position)).getUid());
                break;
            case R.id.order_spinner_partners:
                getDocument().setPartnerUid((getPartners().get(position)).getUid());
                break;
            case R.id.order_spinner_warehouses:
                getDocument().setWarehouseUid((warehouses.get(position)).getUid());
                break;
        }
    }

    @Bindable
    public void setSelectedItemWarehouse(int position) {
        selectedItemWarehouse = position;
    }

    @Bindable
    public int getSelectedItemWarehouse() {
        return selectedItemWarehouse;
    }

    @Bindable
    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableGoods> tableGoods) {
        if (recyclerView.getAdapter() instanceof GoodsOrderRecyclerViewAdapter) {
            ((GoodsOrderRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableGoods);
        }
    }

    public void setOrder() {
        if (getDocument() != null) return;
        setDocument(new Order());
        setTable(getDocument().getTable());
        setAdapter(new GoodsOrderRecyclerViewAdapter(getTable(), this));
        initSpinnersData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void setOrder(String uid) {
        if (getDocument() != null) return;
        setDocument(new Order());
        getDocByUid(uid)
                .andThen(initSpinnersData())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addRow(String goodUid, double price, String goodName) {
        getTable().add(new TableGoods(getDocument().getUid(),
                getTable().size(),
                goodUid,
                goodName,
                1,
                price,
                price));
        getAdapter().notifyItemInserted(getTable().size());
        countSum();
    }

    public Completable getDocByUid(String uid) {
        return Completable.create(subscriber -> {
            DbRoom db = OrderMolder.getApplication().getDatabase();
            setDocument(db.getOrderDao().getByUid(uid));
            setTable(db.getTableGoodsDao().getByUid(uid));
            setAdapter(new GoodsOrderRecyclerViewAdapter(getTable(), this));
            getAdapter().setOnEntryLongClickListener((v, position) -> {
                setSelectedMenuItemPosition(position);
                v.showContextMenu();
                return true;
            });
            notifyPropertyChanged(BR.title);
            subscriber.onComplete();
        });
    }

    public Completable saveDoc(Order document) {
        return Completable.create(subscriber -> {
            checkUid();
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getOrderDao().insertAll(document);
            db.getTableGoodsDao().deleteByUid(document.getUid());
            db.getTableGoodsDao().insertAll(Arrays.copyOf(getTable().toArray(), getTable().size(), TableGoods[].class));
            subscriber.onComplete();
        });
    }

    @Override
    protected void initOthers(DbRoom db) {
        warehouses = new ArrayList<>();
        ListIterator<Warehouse> warehouseListIterator = db.getWarehouseDao().getAll().listIterator();
        while (warehouseListIterator.hasNext()) {
            Warehouse w = warehouseListIterator.next();
            warehouses.add(w);
            if (w.getUid().equals(getDocument().getWarehouseUid())) {
                selectedItemWarehouse = warehouseListIterator.previousIndex();
                notifyPropertyChanged(BR.warehouses);
            }
        }
    }
}
