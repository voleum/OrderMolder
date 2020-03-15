package dev.voleum.ordermolder.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Price extends Obj {

    double price;

    public Price(String uid, double price) {
        super(uid);
        this.price = price;
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_GOOD_UID, uid);
        cv.put(DbHelper.COLUMN_PRICE, price);
        db.insertWithOnConflict(DbHelper.TABLE_PRICE_LIST, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getPriceDao().insertAll(this);
        return false;
    }

    public double getPrice() {
        return price;
    }
}
