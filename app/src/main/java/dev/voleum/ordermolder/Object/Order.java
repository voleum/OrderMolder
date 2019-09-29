package dev.voleum.ordermolder.Object;

public class Order extends Document {

    public Order(int number, String date, Company company, Partner partner, int sum) {
        super(number, date, company, partner, sum);
    }

}
