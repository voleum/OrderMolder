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

    public OrderViewModel(String orderUid) {
        getOrderFromDb(orderUid);
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
    public String getTitle() {
        return order.toString();
    }

    @Bindable
    public void setDate(String date) {
        order.setDate(date);
    }

    @Bindable
    public String getDate() {
        return order.getDate();
    }

    @Bindable
    public void setTime(String time) {
        order.setTime(time);
    }

    @Bindable
    public String getTime() {
        return order.getTime();
    }

    @Bindable
    public String getSum() {
        return String.valueOf(order.getSum());
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<TableGoods> tableGoods) {
        if (recyclerView.getAdapter() instanceof GoodsOrderRecyclerViewAdapter) {
            ((GoodsOrderRecyclerViewAdapter) recyclerView.getAdapter()).setData(tableGoods);
        }
    }

    public void increaseQuantityInRow(int position) {
        tableGoods.get(position).increaseQuantity();
    }

    public void decreaseQuantityInRow(int position) {
        tableGoods.get(position).decreaseQuantity();
    }

    public void countSum() {
        double sum = 0.0;
        for (TableGoods row : tableGoods
        ) {
            sum += row.getSum();
        }
        order.setSum(sum);
        notifyChange();
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

    private void getOrderFromDb(String uid) {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbHelper.COLUMN_UID + " = ?";
        String[] selectionArgs = { uid };
        Cursor c = db.query(DbHelper.TABLE_ORDERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int dateClIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
        int companyClIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
        int partnerClIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
        int warehouseClIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            order = new Order(c.getString(uidClIndex),
                    c.getString(dateClIndex),
                    c.getString(dateClIndex), //FIXME: TIME!!!
                    c.getString(companyClIndex),
                    c.getString(partnerClIndex),
                    c.getString(warehouseClIndex),
                    c.getDouble(sumClIndex));
        }
        c.close();
        db.close();
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

    public boolean saveOrder() {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            order.save(db);
            for (TableGoods row : tableGoods
            ) {
                row.save(db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }
}
