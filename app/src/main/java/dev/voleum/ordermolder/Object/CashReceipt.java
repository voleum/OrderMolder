package dev.voleum.ordermolder.Object;

public class CashReceipt extends Document {

    private String object;

    public CashReceipt(String code, String date, Company company, Partner partner, int sum, String object) {
        super(code, date, company, partner, sum);
        this.object = object;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
