package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Good;
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
        initSpinners();
        df = DecimalHelper.newMoneyFieldFormat();
    }

    public void setOrder(String uid) {
        if (order != null) return;
        order = new Order(uid);
        this.tableGoods = order.getTableGoods();
        this.adapter = new GoodsOrderRecyclerViewAdapter(tableGoods, this);
        initSpinners();
        df = DecimalHelper.newMoneyFieldFormat();
    }

    private void initSpinners() {
        initSpinnersData()
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        notifyPropertyChanged(dev.voleum.ordermolder.BR.entryCompanies);
                        notifyPropertyChanged(dev.voleum.ordermolder.BR.entryPartners);
                        notifyPropertyChanged(dev.voleum.ordermolder.BR.entryWarehouses);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private Completable initSpinnersData() {
        return Completable.create(subscriber -> {
//            DbHelper dbHelper = DbHelper.getInstance();
//            SQLiteDatabase db = dbHelper.getReadableDatabase();
//            Cursor c;
            DbRoom db = OrderMolder.getApplication().getDatabase();
//
//          // region Companies
//            companies = new ArrayList<>();
            companies = db.getCompanyDao().getAll();
//            c = db.query(DbHelper.TABLE_COMPANIES,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (c.moveToFirst()) {
//                int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
//                int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
//                int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
//                do {
//                    Company company = new Company(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
//                    companies.add(company);
//                    if (company.getUid().equals(order.getCompanyUid())) {
//                        selectedItemCompany = companies.indexOf(company);
//                    }
//                } while (c.moveToNext());
//            }
//            // endregion
//
//            // region Partners
//            partners = new ArrayList<>();
            partners = db.getPartnerDao().getAll();
//            c = db.query(DbHelper.TABLE_PARTNERS,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (c.moveToFirst()) {
//                int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
//                int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
//                int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
//                do {
//                    Partner partner = new Partner(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
//                    partners.add(partner);
//                    if (partner.getUid().equals(order.getPartnerUid())) {
//                        selectedItemPartner = partners.indexOf(partner);
//                    }
//                } while (c.moveToNext());
//            }
//            // endregion
//
//            // region Warehouses
//            warehouses = new ArrayList<>();
            warehouses = db.getWarehouseDao().getAll();
//            c = db.query(DbHelper.TABLE_WAREHOUSES,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (c.moveToFirst()) {
//                int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
//                int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
//                do {
//                    Warehouse warehouse = new Warehouse(c.getString(uidClIndex), c.getString(nameClIndex));
//                    warehouses.add(warehouse);
//                    if (warehouse.getUid().equals(order.getWarehouseUid())) {
//                        selectedItemWarehouse = warehouses.indexOf(warehouse);
//                    }
//                } while (c.moveToNext());
//            }
//            // endregion
//
//            c.close();
//            dbHelper.close();
//
            subscriber.onComplete();
        });
    }

    public void countSum() {
        double sum = 0.0;
        for (TableGoods row : tableGoods
        ) {
            sum += row.getSum();
        }
        order.setSum(sum);
        notifyPropertyChanged(dev.voleum.ordermolder.BR.sum);
    }

    public void addGood(Good good, double quantity, double price) {
        tableGoods.add(new TableGoods(order.getUid(),
                tableGoods.size(),
                good.getUid(),
                good.getName(),
                quantity,
                price,
                quantity * price));
        adapter.notifyItemInserted(tableGoods.size());
        countSum();
    }

    public void removeGood(int position) {
        tableGoods.remove(position);
        adapter.notifyItemRemoved(position);
        countSum();
    }

    public Completable saveOrder(Order order) {
        return Completable.create(subscriber -> {
//            DbHelper dbHelper = DbHelper.getInstance();
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            db.beginTransaction();
//            try {
//                if (!order.save(db)) throw new Exception("Saving error!");
//                db.setTransactionSuccessful();
//            } catch (Exception e) {
//                subscriber.onError(e);
//            } finally {
//                db.endTransaction();
//                db.close();
//            }
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getOrderDao().insertAll(order);
            subscriber.onComplete();
        });
    }
}
