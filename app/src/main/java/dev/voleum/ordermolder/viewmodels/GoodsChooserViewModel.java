package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.ui.orders.GoodsChooserActivity;

public class GoodsChooserViewModel extends ViewModelObservable {

    private List<HashMap<String, Object>> goods;
    private GoodsChooserRecyclerViewAdapter adapter;
    private GoodsChooserRecyclerViewAdapter.OnEntryClickListener onEntryClickListener;

    public GoodsChooserViewModel() {

    }

    @Bindable
    public void setAdapter(GoodsChooserRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public GoodsChooserRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<HashMap<String, Object>> getGoods() {
        return goods;
    }

    @BindingAdapter("app:goodsData")
    public static void setData(RecyclerView recyclerView, List<HashMap<String, Object>> goods) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((GoodsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(goods);
        }
    }

    public void init() {
        initGoodList();
    }

    private void initGoodList() {

        goods = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " +
                DbHelper.COLUMN_UID + ", " +
                DbHelper.COLUMN_GROUP_UID + ", " +
                DbHelper.COLUMN_NAME + ", " +
                DbHelper.COLUMN_UNIT_UID + ", " +
                DbHelper.COLUMN_PRICE +
                " FROM " + DbHelper.TABLE_GOODS +
                " LEFT JOIN " + DbHelper.TABLE_PRICE_LIST +
                " ON " + DbHelper.COLUMN_UID + " = " + DbHelper.COLUMN_GOOD_UID +
                " ORDER BY " + DbHelper.COLUMN_NAME, null);

        HashMap<String, Object> values;

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int groupIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
            int priceIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
            do {
                values = new HashMap<>();
                values.put(GoodsChooserActivity.GOOD, new Good(c.getString(uidIndex),
                        c.getString(groupIndex),
                        c.getString(nameIndex),
                        c.getString(unitIndex)));
                values.put(GoodsChooserActivity.PRICE, c.getDouble(priceIndex));
                goods.add(values);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        adapter = new GoodsChooserRecyclerViewAdapter(goods);
        adapter.setOnEntryClickListener(onEntryClickListener);
    }

    public void setOnEntryClickListener(GoodsChooserRecyclerViewAdapter.OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }
}
