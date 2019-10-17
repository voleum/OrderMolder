package dev.voleum.ordermolder.Object;

public class CashReceipt extends Document {

    public CashReceipt() {

    }

    public CashReceipt(String uid, String date, String companyUid, String partnerUid, double sum) {
        super(uid, date, companyUid, partnerUid, sum);
    }
}
