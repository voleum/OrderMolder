package dev.voleum.ordermolder.Object;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.MainActivity;

public class Partner extends Catalog {

    public Partner(String tin, String name) {
        super(tin, name);
    }

    public Partner(String tin) {
        DbAsyncGetPartner dbAsyncGetCompany = new DbAsyncGetPartner();
        dbAsyncGetCompany.execute(tin);
    }

    public String getTin() {
        return code;
    }

    public void setTin(String tin) {
        this.code = tin;
    }

    private class DbAsyncGetPartner extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... tins) {
            DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selection = DbHelper.COLUMN_TIN + " = ?";
            Cursor c = db.query(DbHelper.TABLE_PARTNERS, null, selection, tins, null, null, null, "1");
            if (c.moveToFirst()) {
                code = c.getString(c.getColumnIndex(DbHelper.COLUMN_TIN));
                name = c.getString(c.getColumnIndex(DbHelper.COLUMN_NAME));
            }
            c.close();
            db.close();
            return null;
        }
    }
}
