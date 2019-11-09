// TODO: Delete this class and call in MainActivity

package dev.voleum.ordermolder.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.UUID;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;

public class DbAsyncTestData extends AsyncTask<DbHelper, Void, Boolean> {

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Toast.makeText(OrderMolder.getContext(), R.string.snackbar_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(DbHelper... dbHelpers) {
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
        db.delete(DbHelper.TABLE_CASH_RECEIPTS, null, null);
        db.delete(DbHelper.TABLE_OBJECTS_TABLE, null, null);
        db.delete(DbHelper.TABLE_PRICE_LIST, null, null);
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 3; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_TIN, "00000" + (i+1));
            cv.put(DbHelper.COLUMN_NAME, "Company " + (i+1));
            db.insert(DbHelper.TABLE_COMPANIES, null, cv);
        }
        for (int i = 0; i < 10; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_TIN, "00000" + (i+1));
            cv.put(DbHelper.COLUMN_NAME, "Partner " + (i+1));
            db.insert(DbHelper.TABLE_PARTNERS, null, cv);
        }
        String unitUid = getNewUid();
        for (int i = 0; i < 1; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, unitUid);
            cv.put(DbHelper.COLUMN_CODE, 737);
            cv.put(DbHelper.COLUMN_NAME, "шт");
            cv.put(DbHelper.COLUMN_FULL_NAME, "Штука");
            db.insert(DbHelper.TABLE_UNITS, null, cv);
        }
        String[] groupUids = new String[5];
        for (int i = 0; i < groupUids.length; i++) {
            groupUids[i] = getNewUid();
        }
        for (int i = 0; i < 5; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, groupUids[i]);
            cv.put(DbHelper.COLUMN_NAME, "Group " + (i+1));
            db.insert(DbHelper.TABLE_GOODS_GROUPS, null, cv);
        }
        for (int i = 0; i < 20; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_NAME, "Good " + (i+1));
            cv.put(DbHelper.COLUMN_GROUP_UID, groupUids[(i+1)%5]);
            cv.put(DbHelper.COLUMN_UNIT_UID, unitUid);
            db.insert(DbHelper.TABLE_GOODS, null, cv);
        }
        for (int i = 0; i < 2; i++) {
            cv.clear();
            cv.put(DbHelper.COLUMN_UID, getNewUid());
            cv.put(DbHelper.COLUMN_NAME, "Warehouse " + (i+1));
            db.insert(DbHelper.TABLE_WAREHOUSES, null, cv);
        }
        dbHelper.close();
        return null;
    }

    private String getNewUid() {
        return UUID.randomUUID().toString();
    }
}
