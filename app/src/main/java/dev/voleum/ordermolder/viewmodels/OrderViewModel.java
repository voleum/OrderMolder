package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.TableGoods;
import dev.voleum.ordermolder.objects.Warehouse;

@RequiresApi(api = Build.VERSION_CODES.N)
public class OrderViewModel extends BaseObservable implements Spinner.OnItemSelectedListener {

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OrderViewModel() {
        order = new Order();
        this.tableGoods = order.getTableGoods();
        adapter = new GoodsOrderRecyclerViewAdapter(tableGoods, this);
        initSpinnersData();
        setDecimalFormat();
    }

    public OrderViewModel(String uid) {
        order = new Order(uid);
        this.tableGoods = order.getTableGoods();
        this.adapter = new GoodsOrderRecyclerViewAdapter(tableGoods, this);
        initSpinnersData();
        setDecimalFormat();
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

    private void initSpinnersData() {
        // TODO: Async
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // region Companies
        companies = new ArrayList<>();
        c = db.query(DbHelper.TABLE_COMPANIES,
                null,
                null,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                Company company = new Company(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                companies.add(company);
                if (company.getUid().equals(order.getCompanyUid())) {
                    selectedItemCompany = companies.indexOf(company);
                }
            } while (c.moveToNext());
        }
        // endregion

        // region Partners
        partners = new ArrayList<>();
        c = db.query(DbHelper.TABLE_PARTNERS,
                null,
                null,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                Partner partner = new Partner(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                partners.add(partner);
                if (partner.getUid().equals(order.getPartnerUid())) {
                    selectedItemPartner = partners.indexOf(partner);
                }
            } while (c.moveToNext());
        }
        // endregion

        // region Warehouses
        warehouses = new ArrayList<>();
        c = db.query(DbHelper.TABLE_WAREHOUSES,
                null,
                null,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                Warehouse warehouse = new Warehouse(c.getString(uidClIndex), c.getString(nameClIndex));
                warehouses.add(warehouse);
                if (warehouse.getUid().equals(order.getWarehouseUid())) {
                    selectedItemWarehouse = warehouses.indexOf(warehouse);
                }
            } while (c.moveToNext());
        }
        // endregion

        c.close();
        dbHelper.close();
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

    public void onAddGood(Good good, double quantity, double price) {
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

    // TODO: Async
    public boolean saveOrder() {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (!order.save(db)) return false;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    private void setDecimalFormat() {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        df = new DecimalFormat(format, otherSymbols);
    }
}
