package dev.voleum.ordermolder.Object;

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
}
