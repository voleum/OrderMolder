package dev.voleum.ordermolder.Object;

public class Partner extends Catalog {

    private String tin; // Tax Identification Number

    protected Partner(String number, String name, String tin) {
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
