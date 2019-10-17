package dev.voleum.ordermolder.Object;

import androidx.annotation.NonNull;

public abstract class Catalog extends Obj {

    protected String name;

    protected Catalog() {

    }

    protected Catalog(String uid, String name) {
        super(uid);
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
