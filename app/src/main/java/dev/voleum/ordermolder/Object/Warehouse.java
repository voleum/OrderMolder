package dev.voleum.ordermolder.Object;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.Database.DbHelper;

public class Warehouse extends Catalog {
    public Warehouse(String uid, String name) {
        super(uid, name);
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_NAME, name);
        db.insertWithOnConflict(DbHelper.TABLE_WAREHOUSES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
