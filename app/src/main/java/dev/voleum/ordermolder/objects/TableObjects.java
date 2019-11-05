package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class TableObjects extends Table {

    private String objectUid;
    private double sum;

    public TableObjects(String uid, int position, String objectUid, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.sum = sum;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_CASH_RECEIPT_UID, uid);
        cv.put(DbHelper.COLUMN_POSITION, position);
        cv.put(DbHelper.COLUMN_ORDER_UID, objectUid);
        cv.put(DbHelper.COLUMN_SUM_CREDIT, sum);
        db.insertWithOnConflict(DbHelper.TABLE_OBJECTS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getObjectUid() {
        return objectUid;
    }

    public void setObjectUid(String objectUid) {
        this.objectUid = objectUid;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
