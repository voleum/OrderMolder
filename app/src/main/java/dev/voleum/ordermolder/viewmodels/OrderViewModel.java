package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import dev.voleum.ordermolder.BR;
import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.TableGoods;
import dev.voleum.ordermolder.models.Warehouse;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModelObservable implements Spinner.OnItemSelectedListener {

    private DecimalFormat df;

    private Order order;
    private List<TableGoods> tableGoods;
    private GoodsOrderRecyclerViewAdapter adapter;
    private List<Company> companies;
    private List<Partner> partners;
    private List<Warehouse> warehouses;
    private int selectedItemCompany;
    private int selectedItemPartner;
    private int selectedItemWarehouse;

    private int selectedMenuItemPosition;

    public OrderViewModel() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.order_spinner_companies:
                order.setCompanyUid((companies.get(position)).getUid());
                break;
            case R.id.order_spinner_partners:
                order.setPartnerUid((partners.get(position)).getUid());
                break;
            case R.id.order_spinner_warehouses:
                order.setWarehouseUid((warehouses.get(position)).getUid());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Bindable
    public void setSelectedItemCompany(int position) {
        selectedItemCompany = position;
    }

    @Bindable
    public int getSelectedItemCompany() {
        return selectedItemCompany;
    }

    @Bindable
    public void setSelectedItemPartner(int position) {
        selectedItemPartner = position;
    }

    @Bindable
    public int getSelectedItemPartner() {
        return selectedItemPartner;
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
    public Order getOrder() {
        return order;
    }

    @Bindable
    public List<TableGoods> getTableGoods() {
        return tableGoods;
    }

    @Bindable
    public void setAdapter(GoodsOrderRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public GoodsOrderRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public String getTitle() {
        return order.toString();
    }

    @Bindable
    public void setDate(String date) {
        order.setDate(date);
    }

    @Bindable
    public String getDate() {
        return order.getDate();
    }

    @Bindable
    public void setTime(String time) {
        order.setTime(time);
    }

    @Bindable
    public String getTime() {
        return order.getTime();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(order.getSum()));
    }

    @Bindable
    public List<Company> getEntryCompanies() {
        return companies;
    }

    @Bindable
    public List<Partner> getEntryPartners() {
        return partners;
    }

    @Bindable
    public List<Warehouse> getEntryWarehouses() {
        return warehouses;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableGoods> tableGoods) {
        if (recyclerView.getAdapter() instanceof GoodsOrderRecyclerViewAdapter) {
            ((GoodsOrderRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableGoods);
        }
    }

    public void setOrder() {
        if (order != null) return;
        order = new Order();
        this.tableGoods = order.getTableGoods();
        this.adapter = new GoodsOrderRecyclerViewAdapter(tableGoods, this);
        initSpinnersData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        df = DecimalHelper.newMoneyFieldFormat();
    }

    public void setOrder(String uid) {
        if (order != null) return;
        order = new Order();
        getOrderByUid(uid)
                .andThen(initSpinnersData())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
        df = DecimalHelper.newMoneyFieldFormat();
    }

    private Completable initSpinnersData() {
        return Completable.create(subscriber -> {

            DbRoom db = OrderMolder.getApplication().getDatabase();

            companies = new ArrayList<>();
            ListIterator<Company> companyListIterator = db.getCompanyDao().getAll().listIterator();
            while (companyListIterator.hasNext()) {
                Company c = companyListIterator.next();
                companies.add(c);
                if (c.getUid().equals(order.getCompanyUid())) {
                    selectedItemCompany = companyListIterator.previousIndex();
                }
            }

            partners = new ArrayList<>();
            ListIterator<Partner> partnerListIterator = db.getPartnerDao().getAll().listIterator();
            while (partnerListIterator.hasNext()) {
                Partner p = partnerListIterator.next();
                partners.add(p);
                if (p.getUid().equals(order.getPartnerUid())) {
                    selectedItemPartner = partnerListIterator.previousIndex();
                }
            }

            warehouses = new ArrayList<>();
            ListIterator<Warehouse> warehouseListIterator = db.getWarehouseDao().getAll().listIterator();
            while (warehouseListIterator.hasNext()) {
                Warehouse w = warehouseListIterator.next();
                warehouses.add(w);
                if (w.getUid().equals(order.getWarehouseUid())) {
                    selectedItemWarehouse = warehouseListIterator.previousIndex();
                }
            }

            notifyPropertyChanged(BR.entryCompanies);
            notifyPropertyChanged(BR.entryPartners);
            notifyPropertyChanged(BR.entryWarehouses);
        });
    }

    public void countSum() {
        double sum = 0.0;
        for (TableGoods row : tableGoods
        ) {
            sum += row.getSum();
        }

        BigDecimal bd = BigDecimal.valueOf(sum);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        sum = bd.doubleValue();

        order.setSum(sum);
        notifyPropertyChanged(dev.voleum.ordermolder.BR.sum);
    }

    public void addRow(String goodUid, double price, String goodName) {
        tableGoods.add(new TableGoods(order.getUid(),
                tableGoods.size(),
                goodUid,
                goodName,
                1,
                price,
                price));
        adapter.notifyItemInserted(tableGoods.size());
        countSum();
    }

    public void removeGood() {
        tableGoods.remove(selectedMenuItemPosition);
        adapter.notifyItemRemoved(selectedMenuItemPosition);
        countSum();
    }

    public Completable getOrderByUid(String uid) {
        return Completable.create(subscriber -> {
            DbRoom db = OrderMolder.getApplication().getDatabase();
            order = db.getOrderDao().getByUid(uid);
            tableGoods = db.getTableGoodsDao().getByUid(uid);
            adapter = new GoodsOrderRecyclerViewAdapter(tableGoods, this);
            adapter.setOnEntryLongClickListener((v, position) -> {
                selectedMenuItemPosition = position;
                v.showContextMenu();
            });
            notifyPropertyChanged(BR.title);
            subscriber.onComplete();
        });
    }

    public Completable saveOrder(Order order) {
        return Completable.create(subscriber -> {
            if (order.getUid().isEmpty()) {
                String uid = UUID.randomUUID().toString();
                order.setUid(uid);
                for (int i = 0; i < tableGoods.size(); i++) {
                    tableGoods.get(i).setUid(uid);
                }
            }
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getOrderDao().insertAll(order);
            db.getTableGoodsDao().insertAll(Arrays.copyOf(tableGoods.toArray(), tableGoods.size(), TableGoods[].class));
            subscriber.onComplete();
        });
    }
}
