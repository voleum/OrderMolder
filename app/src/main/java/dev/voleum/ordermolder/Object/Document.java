package dev.voleum.ordermolder.Object;

import androidx.annotation.NonNull;

abstract class Document extends Obj {

    protected String date;
    protected Company company;
    protected Partner partner;
    protected double sum;

    protected Document() {

    }

    protected Document(String uid, String date, Company company, Partner partner, double sum) {
        super(uid);
        this.date = date;
        this.company = company;
        this.partner = partner;
        this.sum = sum;
    }

    @NonNull
    @Override
    public String toString() {
        return date + " / " + uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
