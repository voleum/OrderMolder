package dev.voleum.ordermolder.models;

import androidx.room.PrimaryKey;

public abstract class Table extends Obj {

    @PrimaryKey protected int position;
    protected double sum;

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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

}
