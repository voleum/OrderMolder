package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class Unit extends Catalog {

    private int code;
    private String fullName;

    public Unit(String uid, int code, String name, String fullName) {
        super(uid, name);
        this.code = code;
        this.fullName = fullName;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_CODE, code);
        cv.put(DbHelper.COLUMN_NAME, name);
        cv.put(DbHelper.COLUMN_FULL_NAME, fullName);
        db.insertWithOnConflict(DbHelper.TABLE_UNITS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}