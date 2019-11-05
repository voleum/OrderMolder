package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class Price extends Obj {

    double price;

    public Price(String uid, double price) {
        super(uid);
        this.price = price;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_GOOD_UID, uid);
        cv.put(DbHelper.COLUMN_PRICE, price);
        db.insertWithOnConflict(DbHelper.TABLE_PRICE_LIST, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public double getPrice() {
        return price;
    }
}
