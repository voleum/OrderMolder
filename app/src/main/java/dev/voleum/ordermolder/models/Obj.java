package dev.voleum.ordermolder.models;

import android.icu.text.DecimalFormat;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.DecimalHelper;

public abstract class Obj implements Serializable {

    @Ignore
    protected DecimalFormat moneyDecimalFormat;

    @PrimaryKey
    @NotNull
    protected String uid;

    protected Obj() {
        moneyDecimalFormat = DecimalHelper.Companion.moneyFieldFormat();
    }

    protected Obj(String uid) {
        this.uid = uid;
        moneyDecimalFormat = DecimalHelper.Companion.moneyFieldFormat();
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public abstract boolean save(DbRoom db);
}
