package dev.voleum.ordermolder.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Partner extends EconomicEntity {

    public Partner(String uid, String name, String tin) {
        super(uid, name, tin);
    }

    @Ignore
    public Partner(String tin) {
        DbAsyncGetData dbAsyncGetData = new DbAsyncGetData();
        dbAsyncGetData.execute(tin);
    }

    @Override
    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_NAME, name);
        cv.put(DbHelper.COLUMN_TIN, tin);
        db.insertWithOnConflict(DbHelper.TABLE_PARTNERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getPartnerDao().insertAll(this);
        return true;
    }

    private class DbAsyncGetData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... uids) {
            if (uids.length > 0) {
                DbHelper dbHelper = DbHelper.getInstance();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String selection = DbHelper.COLUMN_TIN + " = ?";
                Cursor c = db.query(DbHelper.TABLE_PARTNERS,
                        null,
                        selection,
                        uids,
                        null,
                        null,
                        null,
                        "1");
                if (c.moveToFirst()) {
                    uid = c.getString(c.getColumnIndex(DbHelper.COLUMN_UID));
                    tin = c.getString(c.getColumnIndex(DbHelper.COLUMN_TIN));
                    name = c.getString(c.getColumnIndex(DbHelper.COLUMN_NAME));
                }
                c.close();
                db.close();
            }
            return null;
        }
    }
}
