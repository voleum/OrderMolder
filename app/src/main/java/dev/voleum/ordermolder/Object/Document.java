package dev.voleum.ordermolder.Object;

import java.io.Serializable;

abstract class Document implements Serializable {

    protected String code;
    protected String date;
    protected Company company;
    protected Partner partner;
    protected double sum;


    protected Document(String code, String date, Company company, Partner partner, double sum) {
        this.code = code;
        this.date = date;
        this.company = company;
        this.partner = partner;
        this.sum = sum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void setSum(int sum) {
        this.sum = sum;
    }
}
