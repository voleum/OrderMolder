package dev.voleum.ordermolder.Object;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.MainActivity;

public class Company extends EconomicEntity {

    public Company(String uid, String name, String tin) {
        super(uid, name, tin);
    }

    public Company(String tin) {
        DbAsyncGetData dbAsyncGetData = new DbAsyncGetData();
        dbAsyncGetData.execute(tin);
    }

    private class DbAsyncGetData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... uids) {
            if (uids.length > 0) {
                DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String selection = DbHelper.COLUMN_TIN + " = ?";
                Cursor c = db.query(DbHelper.TABLE_COMPANIES,
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
