// TODO: Delete this class and call in MainActivity

package dev.voleum.ordermolder.Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.UUID;

public class DbAsyncTestData extends AsyncTask<DbHelper, Void, Void> {
    @Override
    protected Void doInBackground(DbHelper... dbHelpers) {
        DbHelper dbHelper = dbHelpers[0];
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_COMPANIES, null, null);
        db.delete(DbHelper.TABLE_PARTNERS, null, null);
        db.delete(DbHelper.TABLE_WAREHOUSES, null, null);
        db.delete(DbHelper.TABLE_GOODS, null, null);
        db.delete(DbHelper.TABLE_UNITS, null, null);
        db.delete(DbHelper.TABLE_GOODS_GROUPS, null, null);
        db.delete(DbHelper.TABLE_ORDERS, null, null);
        db.delete(DbHelper.TABLE_GOODS_TABLE, null, null);
        ContentValues cv = new ContentValues();
        for (int i = 1; i <= 3; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_TIN, "00000" + i);
            cv.put(DbHelper.COLUMN_NAME, "Company " + i);
            db.insert(DbHelper.TABLE_COMPANIES, null, cv);
        }
        for (int i = 1; i <= 10; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_TIN, "00000" + i);
            cv.put(DbHelper.COLUMN_NAME, "Partner " + i);
            db.insert(DbHelper.TABLE_PARTNERS, null, cv);
        }
        String unitUid = getNewUid();
        for (int i = 1; i <= 1; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, unitUid);
            cv.put(DbHelper.COLUMN_CODE, 737);
            cv.put(DbHelper.COLUMN_NAME, "шт");
            db.insert(DbHelper.TABLE_UNITS, null, cv);
        }
        for (int i = 1; i <= 5; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_NAME, "Group " + i);
            db.insert(DbHelper.TABLE_GOODS_GROUPS, null, cv);
        }
        for (int i = 1; i <= 20; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_NAME, "Good " + i);
            cv.put(DbHelper.COLUMN_GROUP_UID, "UT-00000" + i%5);
            cv.put(DbHelper.COLUMN_UNIT_UID, unitUid);
            db.insert(DbHelper.TABLE_GOODS, null, cv);
        }
        for (int i = 1; i <= 2; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_NAME, "Warehouse " + i);
            db.insert(DbHelper.TABLE_WAREHOUSES, null, cv);
        }
        dbHelper.close();
        return null;
    }

    private String getNewUid() {
        return UUID.randomUUID().toString();
    }
}
