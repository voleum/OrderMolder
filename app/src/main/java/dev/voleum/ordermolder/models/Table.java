package dev.voleum.ordermolder.models;

import androidx.room.PrimaryKey;

public abstract class Table extends Obj {

    @PrimaryKey protected int position;
    protected String uid;

    public Table(String uid, int position) {
        super(uid);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
