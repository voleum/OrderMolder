package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.DocListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.enums.DocumentTypes;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.models.Order;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DocListViewModel<T extends Document> extends BaseObservable {

    private List<T> docs;
    private DocListRecyclerViewAdapter adapter;
    private DocumentTypes docType;
    private int recyclerPosition;

    public DocListViewModel(DocumentTypes docType) {
        this.docType = docType;
        initDocList();
    }

    @Bindable
    public void setAdapter(DocListRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public DocListRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<T> getDocs() {
        return docs;
    }

    @BindingAdapter("data")
    public static void setData(RecyclerView recyclerView, List<? extends Document> docs) {
        if (recyclerView.getAdapter() instanceof DocListRecyclerViewAdapter) {
            ((DocListRecyclerViewAdapter) recyclerView.getAdapter()).setData(docs);
        }
    }

    public void addDoc(T doc) {
        docs.add(doc);
        adapter.notifyItemInserted(docs.size() - 1);
    }

    public void editDoc(T doc, int position) {
        docs.set(position, doc);
        adapter.notifyItemChanged(position);
    }

    public void removeDoc() {
        deleteDocFromDb(docs.get(recyclerPosition).getUid())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void initDocList() {

        DbRoom db = OrderMolder.getApplication().getDatabase();

        switch (docType) {
            case ORDER:
                docs = (List<T>) db.getOrderDao().getAll();
                break;
            case CASH_RECEIPT:
                docs = (List<T>) db.getCashReceiptDao().getAll();
                break;
        }

        adapter = new DocListRecyclerViewAdapter(docs);

        adapter.setOnEntryLongClickListener((v, position) -> {
            recyclerPosition = position;
            v.showContextMenu();
        });
    }

    private Completable deleteDocFromDb(String uid) {
        return Completable.create(subscriber -> {

            DbRoom db = OrderMolder.getApplication().getDatabase();

            switch (docType) {
                case ORDER:
                    db.getOrderDao().deleteAll((Order) docs.get(recyclerPosition));
                    break;
                case CASH_RECEIPT:
                    db.getCashReceiptDao().deleteAll((CashReceipt) docs.get(recyclerPosition));
                    break;
            }

            docs.remove(recyclerPosition);
            adapter.notifyItemRemoved(recyclerPosition);
        });
    }
}
