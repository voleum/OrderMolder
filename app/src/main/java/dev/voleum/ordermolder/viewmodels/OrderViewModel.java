package dev.voleum.ordermolder.viewmodels;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OrderViewModel() {
        order = new Order();
        this.tableGoods = order.getTableGoods();
        adapter = new GoodsOrderRecyclerViewAdapter(tableGoods);
    }

    public OrderViewModel(String uid) {
        order = new Order(uid);
        this.tableGoods = order.getTableGoods();
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
                good.getName(),
                quantity,
                price,
                quantity * price));
        adapter.notifyItemInserted(tableGoods.size());
        countSum();
    }

    public boolean saveOrder() {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            order.save(db);
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
