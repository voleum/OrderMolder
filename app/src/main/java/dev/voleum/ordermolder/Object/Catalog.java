package dev.voleum.ordermolder.Object;

import androidx.annotation.NonNull;

abstract class Catalog {

    protected String number;
    protected String name;

    protected Catalog(String number, String name) {
        this.number = number;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
