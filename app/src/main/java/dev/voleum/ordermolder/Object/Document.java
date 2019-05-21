package dev.voleum.ordermolder.Object;

abstract class Document {

    protected int number;
    protected String date;
    protected Company company;
    protected Partner partner;
    protected int sum;


    protected Document(int number, String date, Company company, Partner partner, int sum) {
        this.number = number;
        this.date = date;
        this.company = company;
        this.partner = partner;
        this.sum = sum;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
