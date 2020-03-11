package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.adapters.DocListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.enums.DocumentTypes;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.models.Order;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static dev.voleum.ordermolder.enums.DocumentTypes.ORDER;

public class DocListViewModel extends BaseObservable {

    private List<Document> docs;
    private DocListRecyclerViewAdapter adapter;
    private DocumentTypes docType;

    public DocListViewModel(DocumentTypes docType) {
        this.docType = docType;
        initDocList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
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
    public List<Document> getDocs() {
        return docs;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<Document> docs) {
        if (recyclerView.getAdapter() instanceof DocListRecyclerViewAdapter) {
            ((DocListRecyclerViewAdapter) recyclerView.getAdapter()).setData(docs);
        }
    }

    public void addDoc(Document doc) {
        docs.add(doc);
        adapter.notifyItemInserted(docs.size() - 1);
    }

    public void editDoc(Document doc, int position) {
        docs.set(position, doc);
        adapter.notifyItemChanged(position);
    }

    public void removeDoc(int position) {
        deleteDocFromDb(docs.get(position).getUid())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        docs.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private Completable initDocList() {

        return Completable.create(subscriber -> {

            docs = new ArrayList<>();

            DbHelper dbHelper = DbHelper.getInstance();
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table;

            switch (docType) {
                case ORDER:
                    table = DbHelper.TABLE_ORDERS;
                    break;
                case CASH_RECEIPT:
                    table = DbHelper.TABLE_CASH_RECEIPTS;
                    break;
                default:
                    return;
            }

            String orderBy = DbHelper.COLUMN_DATE;

            Cursor c = db.query(table,
                    null,
                    null,
                    null,
                    null,
                    null,
                    orderBy);

            if (c.moveToFirst()) {
                int uidIndex = c.getColumnIndex((DbHelper.COLUMN_UID));
                int dateIndex = c.getColumnIndex((DbHelper.COLUMN_DATE));
                int companyIndex = c.getColumnIndex((DbHelper.COLUMN_COMPANY_UID));
                int partnerIndex = c.getColumnIndex((DbHelper.COLUMN_PARTNER_UID));
                int warehouseIndex = -1;
                if (docType == ORDER) {
                    warehouseIndex = c.getColumnIndex((DbHelper.COLUMN_WAREHOUSE_UID));
                }
                int sumIndex = c.getColumnIndex((DbHelper.COLUMN_SUM));
                switch (docType) {
                    case ORDER:
                        do {
                            docs.add(new Order(c.getString(uidIndex),
                                    c.getString(dateIndex),
                                    c.getString(companyIndex),
                                    c.getString(partnerIndex),
                                    c.getString(warehouseIndex),
                                    c.getDouble(sumIndex)));
                        } while (c.moveToNext());
                        break;
                    case CASH_RECEIPT:
                        do {
                            docs.add(new CashReceipt(c.getString(uidIndex),
                                    c.getString(dateIndex),
                                    c.getString(companyIndex),
                                    c.getString(partnerIndex),
                                    c.getDouble(sumIndex)));
                        } while (c.moveToNext());
                        break;
                }
            }
            c.close();
            db.close();
            adapter = new DocListRecyclerViewAdapter(docs);
        });
    }

    private Completable deleteDocFromDb(String uid) {

        return Completable.create(subscriber -> {

            DbHelper dbHelper = DbHelper.getInstance();
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table;

            switch (docType) {
                case ORDER:
                    table = DbHelper.TABLE_ORDERS;
                    break;
                case CASH_RECEIPT:
                    table = DbHelper.TABLE_CASH_RECEIPTS;
                    break;
                default:
                    return;
            }

            String whereClause = DbHelper.COLUMN_UID + " = ?";
            String[] whereArgs = { uid };

            db.delete(table, whereClause, whereArgs);

            db.close();
        });
    }
}
