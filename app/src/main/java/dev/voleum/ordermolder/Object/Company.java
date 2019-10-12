package dev.voleum.ordermolder.Object;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.MainActivity;

public class Company extends Catalog {

    public Company(String tin, String name) {
        super(tin, name);
    }

    public Company(String tin) {
        DbAsyncGetCompany dbAsyncGetCompany = new DbAsyncGetCompany();
        dbAsyncGetCompany.execute(tin);
    }

    public String getTin() {
        return code;
    }

    public void setTin(String tin) {
        this.code = tin;
    }

    private class DbAsyncGetCompany extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... tins) {
            if (tins.length > 0) {
                DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String selection = DbHelper.COLUMN_TIN + " = ?";
                Cursor c = db.query(DbHelper.TABLE_COMPANIES, null, selection, tins, null, null, null, "1");
                if (c.moveToFirst()) {
                    code = c.getString(c.getColumnIndex(DbHelper.COLUMN_TIN));
                    name = c.getString(c.getColumnIndex(DbHelper.COLUMN_NAME));
                }
                c.close();
                db.close();
            }
            return null;
        }
    }

}
