package dev.voleum.ordermolder.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import dev.voleum.ordermolder.Object.Obj;

public class DbAsyncSaveObj extends AsyncTask<Obj, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public DbAsyncSaveObj(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(Obj... objs) {
        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Obj obj: objs
                 ) {
                obj.save(db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return null;
    }
}
