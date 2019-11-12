package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class TableGoods extends Table {

    private String goodUid;
    private String goodName;
    private double quantity;
    private double price;
    private double sum;

    public TableGoods(String uid, int position, String goodUid, double quantity, double price, double sum) {
        super(uid, position);
        this.goodUid = goodUid;
        this.goodName = goodUid;
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
    }

    public TableGoods(String uid, int position, String goodUid, String goodName, double quantity, double price, double sum) {
        super(uid, position);
        this.goodUid = goodUid;
        this.goodName = goodName;
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_ORDER_UID, uid);
        cv.put(DbHelper.COLUMN_POSITION, position);
        cv.put(DbHelper.COLUMN_GOOD_UID, goodUid);
        cv.put(DbHelper.COLUMN_QUANTITY, quantity);
        cv.put(DbHelper.COLUMN_PRICE, price);
        cv.put(DbHelper.COLUMN_SUM, sum);
        db.insertWithOnConflict(DbHelper.TABLE_GOODS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public String getGoodUid() {
        return goodUid;
    }

    public String getGoodNane() {
        return goodName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void countSum() {
        sum = quantity * price;
    }

    public void increaseQuantity() {
        quantity++;
        countSum();
    }

    public void decreaseQuantity() {
        quantity--;
        countSum();
    }
}
