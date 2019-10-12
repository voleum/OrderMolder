package dev.voleum.ordermolder.Object;

public class Order extends Document {

    public Order() {

    }

    public Order(String uid, String date, String companyUid, String partnerUid, double sum) {
        super(uid, date, companyUid, partnerUid, sum);
    }
}
