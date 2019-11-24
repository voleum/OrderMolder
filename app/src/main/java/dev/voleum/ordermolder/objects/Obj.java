package dev.voleum.ordermolder.objects;

import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;

import java.io.Serializable;

import dev.voleum.ordermolder.helpers.DecimalHelper;

public abstract class Obj implements Serializable {

    protected DecimalFormat moneyDecimalFormat;
    protected String uid;

    protected Obj() {
        moneyDecimalFormat = DecimalHelper.newMoneyFieldFormat();
    }

    protected Obj(String uid) {
        this.uid = uid;
        moneyDecimalFormat = DecimalHelper.newMoneyFieldFormat();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public abstract boolean save(SQLiteDatabase db);
}
