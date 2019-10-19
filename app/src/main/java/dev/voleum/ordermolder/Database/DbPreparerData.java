package dev.voleum.ordermolder.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.MainActivity;

public class DbPreparerData {

    public DbPreparerData() {
    }

    public Cursor prepareDoc(String table) {
        DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(table,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor prepareTable(String table, String ownerSelection, String ownerUid) {
        DbHelper dbHelper = DbHelper.getInstance(MainActivity.getAppContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = ownerSelection + " = ?";
        String[] selectionArgs = { ownerUid };
        return db.query(table,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }
}
