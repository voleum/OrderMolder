package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.ObjectsCashReceiptRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.CashReceipt;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.TableObjects;
import io.reactivex.Completable;

public class CashReceiptViewModel extends BaseObservable implements Spinner.OnItemSelectedListener {

    private DecimalFormat df;

    private CashReceipt cashReceipt;
    private List<TableObjects> tableObjects;
    private ObjectsCashReceiptRecyclerViewAdapter adapter;
    private List<Company> companies;
    private List<Partner> partners;
    private int selectedItemCompany;
    private int selectedItemPartner;

    public CashReceiptViewModel() {
        cashReceipt = new CashReceipt();
        this.tableObjects = cashReceipt.getTableObjects();
        adapter = new ObjectsCashReceiptRecyclerViewAdapter(tableObjects, this);
        initSpinnersData();
        setDecimalFormat();
    }

    public CashReceiptViewModel(String uid) {
        cashReceipt = new CashReceipt(uid);
        this.tableObjects = cashReceipt.getTableObjects();
        this.adapter = new ObjectsCashReceiptRecyclerViewAdapter(tableObjects, this);
        initSpinnersData();
        setDecimalFormat();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.cash_receipt_spinner_companies:
                cashReceipt.setCompanyUid((companies.get(position)).getUid());
                break;
            case R.id.cash_receipt_spinner_partners:
                cashReceipt.setPartnerUid((partners.get(position)).getUid());
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
    public CashReceipt getCashReceipt() {
        return cashReceipt;
    }

    @Bindable
    public List<TableObjects> getTableObjects() {
        return tableObjects;
    }

    @Bindable
    public void setAdapter(ObjectsCashReceiptRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public ObjectsCashReceiptRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public String getTitle() {
        return cashReceipt.toString();
    }

    @Bindable
    public void setDate(String date) {
        cashReceipt.setDate(date);
    }

    @Bindable
    public String getDate() {
        return cashReceipt.getDate();
    }

    @Bindable
    public void setTime(String time) {
        cashReceipt.setTime(time);
    }

    @Bindable
    public String getTime() {
        return cashReceipt.getTime();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(cashReceipt.getSum()));
    }

    @Bindable
    public List<Company> getEntryCompanies() {
        return companies;
    }

    @Bindable
    public List<Partner> getEntryPartners() {
        return partners;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableObjects> tableObjects) {
        if (recyclerView.getAdapter() instanceof ObjectsCashReceiptRecyclerViewAdapter) {
            ((ObjectsCashReceiptRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableObjects);
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
                if (company.getUid().equals(cashReceipt.getCompanyUid())) {
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
                if (partner.getUid().equals(cashReceipt.getPartnerUid())) {
                    selectedItemPartner = partners.indexOf(partner);
                }
            } while (c.moveToNext());
        }
        // endregion

        c.close();
        dbHelper.close();
    }

    public void countSum() {
        double sum = 0.0;
        for (TableObjects row : tableObjects
        ) {
            sum += row.getSum();
        }
        cashReceipt.setSum(sum);
        notifyPropertyChanged(dev.voleum.ordermolder.BR.sum);
    }

    public void onAddObject(Order order, Double sum) {
        tableObjects.add(new TableObjects(cashReceipt.getUid(),
                tableObjects.size(),
                order.getUid(),
                order.toString(),
                sum));
        adapter.notifyItemInserted(tableObjects.size());
        countSum();
    }

    public Completable saveCashReceipt() {
        return Completable.create(subscriber -> {
            DbHelper dbHelper = DbHelper.getInstance();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                if (!cashReceipt.save(db)) throw new Exception("Saving error!");
                db.setTransactionSuccessful();
            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                db.endTransaction();
                db.close();
            }
            subscriber.onComplete();
        });
    }

    private void setDecimalFormat() {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        df = new DecimalFormat(format, otherSymbols);
    }
}
