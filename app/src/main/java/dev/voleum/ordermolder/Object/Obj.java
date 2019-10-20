package dev.voleum.ordermolder.Object;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public abstract class Obj implements Serializable {

    protected String uid;

    protected Obj() {

    }

    protected Obj(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public abstract void save(SQLiteDatabase db);
}
