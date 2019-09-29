package dev.voleum.ordermolder.Object;

public class CashReceipt extends Document {

    private String object;

    public CashReceipt(int number, String date, Company company, Partner partner, int sum, String object) {
        super(number, date, company, partner, sum);
        this.object = object;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
