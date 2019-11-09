package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.TableGoods;

public class OrderViewModel extends BaseObservable {

    private Order order;
    private List<TableGoods> tableGoods;
    private GoodsOrderRecyclerViewAdapter adapter;
    private String date;
    private String time;
    private double sum;

    public OrderViewModel(Order order) {
        this.order = order;
        this.date = order.getDate().substring(0, 10).replace("-", ".");
        this.time = order.getDate().substring(11, 19);
        this.sum = order.getSum();
        this.tableGoods = new ArrayList<>();
        fillGoodList(order.getUid());
        this.adapter = new GoodsOrderRecyclerViewAdapter(tableGoods);
    }

    @Bindable
    public Order getOrder() {
        return order;
    }

    @Bindable
    public void setTableGoods(List<TableGoods> tableGoods) {
        this.tableGoods = tableGoods;
    }

    @Bindable
    public List<TableGoods> getTableGoods() {
        return tableGoods;
    }

    @Bindable
    public void setAdapter(GoodsOrderRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public GoodsOrderRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public void setDate(String date) {
        this.date = date;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    @Bindable
    public void setTime(String time) {
        this.time = time;
    }

    @Bindable
    public String getTime() {
        return time;
    }

//    @Bindable
//    public void setSum(String sum) {
//        this.sum = Double.parseDouble(sum);
//    }

    @Bindable
    public String getSum() {
        return String.valueOf(sum);
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableGoods> tableGoods) {
        if (recyclerView.getAdapter() instanceof GoodsOrderRecyclerViewAdapter) {
            ((GoodsOrderRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableGoods);
        }
    }

    private void countSum() {
        sum = 0.0;
        for (TableGoods row : tableGoods
        ) {
            sum += row.getSum();
        }
//        setSum(String.valueOf(sum));
    }

    public void onAddGood(Good good, double quantity, double price) {
        tableGoods.add(new TableGoods(order.getUid(),
                tableGoods.size(),
                good.getUid(),
                quantity,
                price,
                quantity * price));
        adapter.notifyItemInserted(tableGoods.size());
        countSum();
    }

    private void fillGoodList(String uid) {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectionArgs = { uid };
        String sql = "SELECT *"
                + " FROM " + DbHelper.TABLE_GOODS_TABLE
                + " LEFT JOIN " + DbHelper.TABLE_GOODS
                + " ON " + DbHelper.COLUMN_GOOD_UID + " = " + DbHelper.COLUMN_UID
                + " WHERE " + DbHelper.COLUMN_ORDER_UID + " = ?"
                + " ORDER BY " + DbHelper.COLUMN_POSITION;
        Cursor c = db.rawQuery(sql, selectionArgs);
        int positionClIndex = c.getColumnIndex(DbHelper.COLUMN_POSITION);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
        int quantityClIndex = c.getColumnIndex(DbHelper.COLUMN_QUANTITY);
        int priceClIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            do {
                tableGoods.add(new TableGoods(c.getString(uidClIndex),
                        c.getInt(positionClIndex),
                        c.getString(nameClIndex),
                        c.getDouble(quantityClIndex),
                        c.getDouble(priceClIndex),
                        c.getDouble(sumClIndex)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}
