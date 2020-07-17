package dev.voleum.ordermolder.viewmodels;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import dev.voleum.ordermolder.BR;
import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.ObjectsCashReceiptRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.TableObjects;
import io.reactivex.Completable;

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
        notifyPropertyChanged(BR.title);
        initSpinnersData();
    }

    public void setCashReceipt(String uid) {
        if (getDocument() != null) return;
        setDocument(new CashReceipt());
        setTable(getDocument().getTable());
        getDocByUid(uid);
        initSpinnersData();
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

    @NonNull
    public void getDocByUid(String uid) {
            DbRoom db = OrderMolder.getApplication().getDatabase();
            setDocument(db.getCashReceiptDao().getByUid(uid));
            setTable(db.getTableObjectsDao().getByUid(uid));
            setAdapter(new ObjectsCashReceiptRecyclerViewAdapter(getTable(), this));
            notifyPropertyChanged(BR.title);
    }

    @NonNull
    public Completable saveDoc(CashReceipt document) {
        return Completable.create(subscriber -> {
            checkUid();
            numberTable();
            DbRoom db = OrderMolder.getApplication().getDatabase();
            db.getCashReceiptDao().insertAll(document);
            db.getTableObjectsDao().deleteByUid(document.getUid());
            db.getTableObjectsDao().insertAll(Arrays.copyOf(getTable().toArray(), getTable().size(), TableObjects[].class));
            subscriber.onComplete();
        });
    }
}
