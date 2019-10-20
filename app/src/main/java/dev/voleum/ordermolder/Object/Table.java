package dev.voleum.ordermolder.Object;

public abstract class Table extends Obj {

    protected int position;

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
