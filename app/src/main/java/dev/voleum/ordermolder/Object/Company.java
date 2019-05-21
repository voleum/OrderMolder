package dev.voleum.ordermolder.Object;

import androidx.annotation.NonNull;

public class Company extends Catalog {

    private String tin; // Tax Identification Number

    public Company(String number, String name, String tin) {
        super(number, name);
        this.tin = tin;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

}
