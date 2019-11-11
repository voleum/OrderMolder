package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class CashReceipt extends Document {

    public CashReceipt() {

    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_DATE, dateTime);
        cv.put(DbHelper.COLUMN_COMPANY_UID, companyUid);
        cv.put(DbHelper.COLUMN_PARTNER_UID, partnerUid);
        cv.put(DbHelper.COLUMN_SUM, sum);
        db.insertWithOnConflict(DbHelper.TABLE_CASH_RECEIPTS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
