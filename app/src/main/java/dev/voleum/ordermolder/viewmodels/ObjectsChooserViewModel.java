package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.adapters.ObjectsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.ui.cashreceipts.ObjectsChooserActivity;

public class ObjectsChooserViewModel extends BaseObservable {

    private List<HashMap<String, Object>> objects;
    private ObjectsChooserRecyclerViewAdapter adapter;
    private String companyUid;
    private String partnerUid;

    public ObjectsChooserViewModel(String companyUid, String partnerUid) {
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        initObjectsList();
    }

    @Bindable
    public void setAdapter(ObjectsChooserRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public ObjectsChooserRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<HashMap<String, Object>> getObjects() {
        return objects;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<HashMap<String, Object>> objects) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((ObjectsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(objects);
        }
    }

    private void initObjectsList() {

        objects = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy = DbHelper.COLUMN_DATE;
        String[] selectionArgs = {companyUid, partnerUid};
        Cursor c = db.rawQuery("SELECT *" +
                        " FROM " + DbHelper.TABLE_ORDERS +
                        " WHERE " + DbHelper.COLUMN_COMPANY_UID + " = ?" +
                        " AND " + DbHelper.COLUMN_PARTNER_UID + " = ?" +
                        " ORDER BY " + orderBy,
                selectionArgs);

        HashMap<String, Object> values;

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int dateIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
            int companyIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
            int partnerIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
            int warehouseIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
            int sumIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
            do {
                values = new HashMap<>();
                values.put(ObjectsChooserActivity.OBJECT, new Order(c.getString(uidIndex),
                        c.getString(dateIndex),
                        c.getString(companyIndex),
                        c.getString(partnerIndex),
                        c.getString(warehouseIndex),
                        c.getDouble(sumIndex)));
                values.put(ObjectsChooserActivity.SUM, c.getDouble(sumIndex));
                objects.add(values);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        adapter = new ObjectsChooserRecyclerViewAdapter(objects);
    }
}
