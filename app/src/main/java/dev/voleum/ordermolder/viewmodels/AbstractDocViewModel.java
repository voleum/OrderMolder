package dev.voleum.ordermolder.viewmodels;

import android.icu.text.DecimalFormat;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import dev.voleum.ordermolder.BR;
import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.Table;
import io.reactivex.Completable;

abstract public class AbstractDocViewModel<D extends Document, T extends Table, E extends RecyclerView.Adapter> extends ViewModelObservable implements Spinner.OnItemSelectedListener {

    protected DecimalFormat df;

    protected D document;
    protected List<T> table;
    protected E adapter;
    protected List<Company> companies;
    protected List<Partner> partners;
    protected int selectedItemCompany;
    protected int selectedItemPartner;

    protected int selectedMenuItemPosition;

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
    public D getDocument() {
        return document;
    }

    @Bindable
    public List<T> getTable() {
        return table;
    }

    @Bindable
    public void setAdapter(E adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public E getAdapter() {
        return adapter;
    }

    @Bindable
    public String getTitle() {
        return document.toString();
    }

    @Bindable
    public void setDate(String date) {
        document.setDate(date);
    }

    @Bindable
    public String getDate() {
        return document.getDate();
    }

    @Bindable
    public void setTime(String time) {
        document.setTime(time);
    }

    @Bindable
    public String getTime() {
        return document.getTime();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(df.format(document.getSum()));
    }

    @Bindable
    public List<Company> getEntryCompanies() {
        return companies;
    }

    @Bindable
    public List<Partner> getEntryPartners() {
        return partners;
    }

    protected Completable initSpinnersData() {
        return Completable.create(subscriber -> {

            DbRoom db = OrderMolder.getApplication().getDatabase();
            initCompanies(db);
            initPartners(db);
            initOthers(db);
        });
    }

    public void countSum() {
        double sum = 0.0;
        for (T row : table
        ) {
            sum += row.getSum();
        }

        BigDecimal bd = BigDecimal.valueOf(sum);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        sum = bd.doubleValue();

        document.setSum(sum);
        notifyPropertyChanged(BR.sum);
    }

    public void removeRow() {
        table.remove(selectedMenuItemPosition);
        adapter.notifyItemRemoved(selectedMenuItemPosition);
        countSum();
    }

    protected void initCompanies(DbRoom db) {
        companies = new ArrayList<>();
        ListIterator<Company> companyListIterator = db.getCompanyDao().getAll().listIterator();
        while (companyListIterator.hasNext()) {
            Company c = companyListIterator.next();
            companies.add(c);
            if (c.getUid().equals(document.getCompanyUid())) {
                selectedItemCompany = companyListIterator.previousIndex();
                notifyPropertyChanged(BR.entryCompanies);
            }
        }
    }

    protected void initPartners(DbRoom db) {
        partners = new ArrayList<>();
        ListIterator<Partner> partnerListIterator = db.getPartnerDao().getAll().listIterator();
        while (partnerListIterator.hasNext()) {
            Partner p = partnerListIterator.next();
            partners.add(p);
            if (p.getUid().equals(document.getPartnerUid())) {
                selectedItemPartner = partnerListIterator.previousIndex();
                notifyPropertyChanged(BR.entryPartners);
            }
        }
    }

    protected void initOthers(DbRoom db) {

    }

    protected void checkUid() {
        if (document.getUid().isEmpty()) {
            String uid = UUID.randomUUID().toString();
            document.setUid(uid);
            for (int i = 0; i < table.size(); i++) {
                table.get(i).setUid(uid);
            }
        }
    }
}
