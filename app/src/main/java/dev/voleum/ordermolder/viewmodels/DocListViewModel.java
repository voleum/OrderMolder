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
import dev.voleum.ordermolder.helpers.DocListViewModelItemTouchHelper;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.models.Order;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DocListViewModel<T extends Document> extends BaseObservable implements DocListViewModelItemTouchHelper {

    private List<T> docs;
    private DocListRecyclerViewAdapter adapter;
    private DocumentTypes docType;

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

    @Override
    public void onItemDismiss(int position) {
        deleteDocFromDb(docs.get(position))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        docs.remove(position);
                        adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void addDoc(T doc) {
        docs.add(doc);
        adapter.notifyItemInserted(docs.size() - 1);
    }

    public void editDoc(T doc, int position) {
        docs.set(position, doc);
        adapter.notifyItemChanged(position);
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
    }

    private Completable deleteDocFromDb(Document document) {
        return Completable.create(subscriber -> {

            DbRoom db = OrderMolder.getApplication().getDatabase();

            switch (docType) {
                case ORDER:
                    db.getOrderDao().deleteAll((Order) document);
                    db.getTableGoodsDao().deleteByUid(document.getUid());
                    break;
                case CASH_RECEIPT:
                    db.getCashReceiptDao().deleteAll((CashReceipt) document);
                    db.getTableObjectsDao().deleteByUid(document.getUid());
                    break;
            }

            subscriber.onComplete();
        });
    }
}
