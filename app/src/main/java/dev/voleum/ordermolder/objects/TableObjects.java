package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class TableObjects extends Table {

    private String objectUid;
    private String objectName;
    private double sum;

    public TableObjects(String uid, int position, String objectUid, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.objectName = objectUid;
        this.sum = sum;
    }

    public TableObjects(String uid, int position, String objectUid, String objectName, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.objectName = objectName;
        this.sum = sum;
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_CASH_RECEIPT_UID, uid);
        cv.put(DbHelper.COLUMN_POSITION, position);
        cv.put(DbHelper.COLUMN_ORDER_UID, objectUid);
        cv.put(DbHelper.COLUMN_SUM_CREDIT, sum);
        db.insertWithOnConflict(DbHelper.TABLE_OBJECTS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public String getObjectUid() {
        return objectUid;
    }

    public String getObjectName() {
        return objectName;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
