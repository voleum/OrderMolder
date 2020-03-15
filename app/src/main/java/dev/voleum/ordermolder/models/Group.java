package dev.voleum.ordermolder.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Group extends Catalog {

    public Group(String uid, String name){
        super(uid, name);
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_NAME, name);
        db.insertWithOnConflict(DbHelper.TABLE_GOODS_GROUPS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getGroupDao().insertAll(this);
        return true;
    }
}
