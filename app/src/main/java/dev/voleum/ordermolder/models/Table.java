package dev.voleum.ordermolder.models;

import androidx.room.PrimaryKey;

public abstract class Table extends Obj {

    protected String objUid;
    protected String objName;

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

    public String getObjUid() {
        return objUid;
    }

    public void setObjUid(String objUid) {
        this.objUid = objUid;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
