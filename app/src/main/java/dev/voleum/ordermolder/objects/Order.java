package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class Order extends Document {

    private String warehouseUid;

    public Order() {

    }

    public Order(String uid, String date, String companyUid, String partnerUid, String warehouseUid, double sum) {
        super(uid, date, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_DATE, date);
        cv.put(DbHelper.COLUMN_COMPANY_UID, companyUid);
        cv.put(DbHelper.COLUMN_PARTNER_UID, partnerUid);
        cv.put(DbHelper.COLUMN_WAREHOUSE_UID, warehouseUid);
        cv.put(DbHelper.COLUMN_SUM, sum);
        db.insertWithOnConflict(DbHelper.TABLE_ORDERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getWarehouseUid() {
        return warehouseUid;
    }

    public void setWarehouseUid(String warehouseUid) {
        this.warehouseUid = warehouseUid;
    }
}
