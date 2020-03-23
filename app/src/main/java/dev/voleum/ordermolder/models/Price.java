package dev.voleum.ordermolder.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Price extends Obj {

    double price;
    String goodName;

    @Ignore
    public Price(String uid, double price) {
        super(uid);
        this.price = price;
    }

    public Price(String uid, double price, String goodName) {
        super(uid);
        this.price = price;
        this.goodName = goodName;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getPriceDao().insertAll(this);
        return false;
    }

    public double getPrice() {
        return price;
    }

    public String getGoodName() {
        return goodName;
    }

    @NonNull
    @Override
    public String toString() {
        return goodName;
    }
}
