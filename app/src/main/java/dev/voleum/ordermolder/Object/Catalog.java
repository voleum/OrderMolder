package dev.voleum.ordermolder.Object;

import androidx.annotation.NonNull;

import java.io.Serializable;

abstract class Catalog implements Serializable {

    protected String code;
    protected String name;

    protected Catalog() {
    }

    protected Catalog(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
