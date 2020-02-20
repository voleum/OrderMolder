package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.voleum.ordermolder.database.DbHelper;

public class CashReceipt extends Document {

    private List<TableObjects> tableObjects;

    public CashReceipt() {
        setCurrentDate();
        setCurrentTime();
        tableObjects = new ArrayList<>();
        uid = companyUid = partnerUid = "";
        sum = 0;
    }

    public CashReceipt(String uid) {
        getDocFromDb(uid);
    }

    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.tableObjects = new ArrayList<>();
    }

    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.tableObjects = tableObjects;
    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.tableObjects = new ArrayList<>();
    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.tableObjects = tableObjects;
    }

    @Override
    protected void getDocFromDb(String uid) {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] selectionArgs = { uid };
        String sql = "SELECT *"
                + " FROM " + DbHelper.TABLE_OBJECTS_TABLE
                + " LEFT JOIN " + DbHelper.TABLE_ORDERS
                + " ON " + DbHelper.COLUMN_ORDER_UID + " = " + DbHelper.COLUMN_UID
                + " WHERE " + DbHelper.COLUMN_CASH_RECEIPT_UID + " = ?"
                + " ORDER BY " + DbHelper.COLUMN_POSITION;
        Cursor c = db.rawQuery(sql, selectionArgs);
        int positionClIndex = c.getColumnIndex(DbHelper.COLUMN_POSITION);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int dateTimeIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM_CREDIT);
        if (c.moveToFirst()) {
            tableObjects = new ArrayList<>();
            do {
                String date = c.getString(dateTimeIndex).substring(0, 10).replace("-", ".");
                String time = c.getString(dateTimeIndex).substring(11, 19);
                tableObjects.add(new TableObjects(uid,
                        c.getInt(positionClIndex),
                        c.getString(uidClIndex),
                        "Date: " + date + " " + time,
                        c.getDouble(sumClIndex)));
            } while (c.moveToNext());
        }

        String selection = DbHelper.COLUMN_UID + " = ?";
        c = db.query(DbHelper.TABLE_CASH_RECEIPTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int dateClIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
        int companyClIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
        int partnerClIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
        sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            this.uid = c.getString(uidClIndex);
            date = c.getString(dateClIndex).substring(0, 10).replace("-", ".");
            time = c.getString(dateClIndex).substring(11, 19);
            companyUid = c.getString(companyClIndex);
            partnerUid = c.getString(partnerClIndex);
            sum = c.getDouble(sumClIndex);
        }

        c.close();
        db.close();
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        try {
            if (tableObjects.isEmpty()) return false;
            if (uid.isEmpty()) setUid(UUID.randomUUID().toString());
            ContentValues cv = new ContentValues();
            String dateTime = date.replace(".", "-") + " " + time + ".000";
            cv.put(DbHelper.COLUMN_UID, uid);
            cv.put(DbHelper.COLUMN_DATE, dateTime);
            cv.put(DbHelper.COLUMN_COMPANY_UID, companyUid);
            cv.put(DbHelper.COLUMN_PARTNER_UID, partnerUid);
            cv.put(DbHelper.COLUMN_SUM, sum);
            db.insertWithOnConflict(DbHelper.TABLE_CASH_RECEIPTS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            String whereClause = DbHelper.COLUMN_CASH_RECEIPT_UID + " = ?";
            String[] whereArgs = { uid };
            db.delete(DbHelper.TABLE_OBJECTS_TABLE,
                    whereClause,
                    whereArgs);
            for (TableObjects t : tableObjects
            ) {
                t.setUid(uid);
                t.save(db);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TableObjects> getTableObjects() {
        return tableObjects;
    }

    public void addObject(TableObjects row) {
        tableObjects.add(row);
    }
}
