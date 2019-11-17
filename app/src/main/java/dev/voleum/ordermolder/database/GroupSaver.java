package dev.voleum.ordermolder.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.objects.Obj;

public class GroupSaver {

    private ArrayList<? extends Obj> arrayList;

    public GroupSaver(ArrayList<? extends Obj> arrayList) {
        this.arrayList = arrayList;
    }

    public void save() {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Obj obj: arrayList
                 ) {
                obj.save(db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
