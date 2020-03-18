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
import dev.voleum.ordermolder.adapters.ObjectsCashReceiptRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.TableObjects;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CashReceiptViewModel extends ViewModelObservable implements Spinner.OnItemSelectedListener {

    private DecimalFormat df;

    private CashReceipt cashReceipt;
    private List<TableObjects> tableObjects;
    private ObjectsCashReceiptRecyclerViewAdapter adapter;
    private List<Company> companies;
    private List<Partner> partners;
    private int selectedItemCompany;
    private int selectedItemPartner;

    private int selectedMenuItemPosition;

    public CashReceiptViewModel() {

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

    public void setCashReceipt() {
        if (cashReceipt != null) return;
        cashReceipt = new CashReceipt();
        this.tableObjects = cashReceipt.getTable();
        adapter = new ObjectsCashReceiptRecyclerViewAdapter(tableObjects, this);
        initSpinnersData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        df = DecimalHelper.newMoneyFieldFormat();
    }

    public void setCashReceipt(String uid) {
        if (cashReceipt != null) return;
        cashReceipt = new CashReceipt();
        this.tableObjects = cashReceipt.getTable();
        getCashReceiptByUid(uid)
                .andThen(initSpinnersData())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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
                if (c.getUid().equals(cashReceipt.getCompanyUid())) {
                    selectedItemCompany = companyListIterator.previousIndex();
                }
            }

            partners = new ArrayList<>();
            ListIterator<Partner> partnerListIterator = db.getPartnerDao().getAll().listIterator();
            while (partnerListIterator.hasNext()) {
                Partner p = partnerListIterator.next();
                partners.add(p);
                if (p.getUid().equals(cashReceipt.getPartnerUid())) {
                    selectedItemPartner = partnerListIterator.previousIndex();
                }
            }

            notifyPropertyChanged(BR.entryCompanies);
            notifyPropertyChanged(BR.entryPartners);
        });
    }

    public void countSum() {
        double sum = 0.0;
        for (TableObjects row : tableObjects
        ) {
            sum += row.getSum();
        }

        BigDecimal bd = BigDecimal.valueOf(sum);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        sum = bd.doubleValue();

        cashReceipt.setSum(sum);
        notifyPropertyChanged(dev.voleum.ordermolder.BR.sum);
    }

    public void addObject(Order order) {
        tableObjects.add(new TableObjects(cashReceipt.getUid(),
                tableObjects.size(),
                order.getUid(),
                order.toString(),
                order.getSum()));
        adapter.notifyItemInserted(tableObjects.size());
        countSum();
    }

    public void removeObject(int position) {
        tableObjects.remove(position);
        adapter.notifyItemRemoved(position);
        countSum();
    }

    public Completable getCashReceiptByUid(String uid) {
        return Completable.create(subscriber -> {
            DbRoom db = OrderMolder.getApplication().getDatabase();
            cashReceipt = db.getCashReceiptDao().getByUid(uid);
            tableObjects = db.getTableObjectsDao().getByUid(uid);
            adapter = new ObjectsCashReceiptRecyclerViewAdapter(tableObjects, this);
            adapter.setOnEntryLongClickListener((v, position) -> {
                selectedMenuItemPosition = position;
                v.showContextMenu();
            });
            notifyPropertyChanged(BR.title);
            subscriber.onComplete();
        });
    }

    public Completable saveCashReceipt(CashReceipt cashReceipt) {
        return Completable.create(subscriber -> {
            if (cashReceipt.getUid().isEmpty()) {
                String uid = UUID.randomUUID().toString();
                cashReceipt.setUid(uid);
                for (int i = 0; i < tableObjects.size(); i++) {
                    tableObjects.get(i).setUid(uid);
                }
            }
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getCashReceiptDao().insertAll(cashReceipt);
            db.getTableObjectsDao().insertAll(Arrays.copyOf(tableObjects.toArray(), tableObjects.size(), TableObjects[].class));
            subscriber.onComplete();
        });
    }
}
