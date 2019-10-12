package dev.voleum.ordermolder.Object;

public class Order extends Document {

    public Order(String code, String date, Company company, Partner partner, double sum) {
        super(code, date, company, partner, sum);
    }

}
