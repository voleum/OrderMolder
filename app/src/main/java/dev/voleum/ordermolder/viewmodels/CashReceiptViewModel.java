package dev.voleum.ordermolder.viewmodels;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import dev.voleum.ordermolder.BR;
import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.ObjectsCashReceiptRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.TableObjects;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CashReceiptViewModel extends AbstractDocViewModel<CashReceipt, TableObjects, ObjectsCashReceiptRecyclerViewAdapter> implements Spinner.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.cash_receipt_spinner_companies:
                getDocument().setCompanyUid((getCompanies().get(position)).getUid());
                break;
            case R.id.cash_receipt_spinner_partners:
                getDocument().setPartnerUid((getPartners().get(position)).getUid());
                break;
        }
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableObjects> tableObjects) {
        if (recyclerView.getAdapter() instanceof ObjectsCashReceiptRecyclerViewAdapter) {
            ((ObjectsCashReceiptRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableObjects);
        }
    }

    public void setCashReceipt() {
        if (getDocument() != null) return;
        setDocument(new CashReceipt());
        setTable(getDocument().getTable());
        setAdapter(new ObjectsCashReceiptRecyclerViewAdapter(getTable(), this));
        initSpinnersData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void setCashReceipt(String uid) {
        if (getDocument() != null) return;
        setDocument(new CashReceipt());
        setTable(getDocument().getTable());
        getDocByUid(uid)
                .andThen(initSpinnersData())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addRow(Order order) {
        getTable().add(new TableObjects(getDocument().getUid(),
                getTable().size(),
                order.getUid(),
                order.toString(),
                order.getSum()));
        getAdapter().notifyItemInserted(getTable().size());
        countSum();
    }

    public Completable getDocByUid(String uid) {
        return Completable.create(subscriber -> {
            DbRoom db = OrderMolder.getApplication().getDatabase();
            setDocument(db.getCashReceiptDao().getByUid(uid));
            setTable(db.getTableObjectsDao().getByUid(uid));
            setAdapter(new ObjectsCashReceiptRecyclerViewAdapter(getTable(), this));
            getAdapter().setOnEntryLongClickListener((v, position) -> {
                setSelectedMenuItemPosition(position);
                v.showContextMenu();
                return true;
            });
            notifyPropertyChanged(BR.title);
            subscriber.onComplete();
        });
    }

    public Completable saveDoc(CashReceipt document) {
        return Completable.create(subscriber -> {
            checkUid();
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getCashReceiptDao().insertAll(document);
            db.getTableObjectsDao().deleteByUid(document.getUid());
            db.getTableObjectsDao().insertAll(Arrays.copyOf(getTable().toArray(), getTable().size(), TableObjects[].class));
            subscriber.onComplete();
        });
    }
}
