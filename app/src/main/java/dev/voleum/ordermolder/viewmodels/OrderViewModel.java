package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.TableGoods;

public class OrderViewModel extends BaseObservable {

    private Order order;
    private List<TableGoods> tableGoods;

    public OrderViewModel(Order order) {
        this.order = order;
        fillGoodList(order.getUid());
    }

    @Bindable
    public Order getOrder() {
        return order;
    }

    @Bindable
    public List<TableGoods> getTableGoods() {
        return tableGoods;
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
//        int groupClIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
        int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
//        int unitClIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
        int quantityClIndex = c.getColumnIndex(DbHelper.COLUMN_QUANTITY);
        int priceClIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
//            HashMap<String, Object> goodUidHash;
            do {
//                goodUidHash = new HashMap<>();
//                goodUidHash.put("good", new Good(c.getString(uidClIndex),
//                        c.getString(groupClIndex),
//                        c.getString(nameClIndex),
//                        c.getString(unitClIndex)));
//                goodUidHash.put("quantity", c.getDouble(quantityClIndex));
//                goodUidHash.put("price", c.getDouble(priceClIndex));
//                goodUidHash.put("sum", c.getDouble(sumClIndex));
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
