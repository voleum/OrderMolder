package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;

@Entity
public class TableGoods extends Table {

    private double quantity;
    private double price;

    @Ignore
    public TableGoods(String uid, int position, String goodUid, double quantity, double price, double sum) {
        super(uid, position);
        this.objUid = goodUid;
        this.objName = goodUid;
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
    }

    public TableGoods(String uid, int position, String objUid, String objName, double quantity, double price, double sum) {
        super(uid, position);
        this.objUid = objUid;
        this.objName = objName;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getTableGoodsDao().insertAll(this);
        return true;
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

    public void countSum() {
        sum = DecimalHelper.Companion.round(quantity * price, 2);
    }

    public void increaseQuantity() {
        quantity++;
        countSum();
    }

    public void decreaseQuantity() {
        if (quantity >= 1) {
            quantity--;
            countSum();
        }
    }
}
