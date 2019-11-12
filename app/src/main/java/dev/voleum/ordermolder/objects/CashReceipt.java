package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.database.DbHelper;

public class CashReceipt extends Document {

    public CashReceipt() {

    }

    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid, dateTime, companyUid, partnerUid, sum);
    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        String dateDb = date.replace(".", "-") + " " + time + ".000";
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_DATE, dateDb);
        cv.put(DbHelper.COLUMN_COMPANY_UID, companyUid);
        cv.put(DbHelper.COLUMN_PARTNER_UID, partnerUid);
        cv.put(DbHelper.COLUMN_SUM, sum);
        db.insertWithOnConflict(DbHelper.TABLE_CASH_RECEIPTS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }
}
