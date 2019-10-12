package dev.voleum.ordermolder.Object;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.MainActivity;

public class Partner extends Catalog {

    private String tin;

    public Partner(String uid, String name, String tin) {
        super(uid, name);
        this.tin = tin;
    }

    public Partner(String tin) {
        DbAsyncGetPartner dbAsyncGetCompany = new DbAsyncGetPartner();
        dbAsyncGetCompany.execute(tin);
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    private class DbAsyncGetPartner extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... uids) {
            if (uids.length > 0) {
                DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String selection = DbHelper.COLUMN_TIN + " = ?";
                Cursor c = db.query(DbHelper.TABLE_PARTNERS, null, selection, uids, null, null, null, "1");
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
