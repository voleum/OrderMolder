package dev.voleum.ordermolder.objects;

import androidx.annotation.NonNull;

public abstract class Document extends Obj {

    protected String date;
    protected String companyUid;
    protected String partnerUid;
    protected double sum;

    protected Document() {

    }

    protected Document(String uid, String date, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.date = date;
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
    }

    @NonNull
    @Override
    public String toString() {
        String dateStr = date
                .substring(0, 10)
                .replace("-", ".");
        return "Date: " + dateStr + " / Sum: " + sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyUid() {
        return companyUid;
    }

    public void setCompanyUid(String companyUid) {
        this.companyUid = companyUid;
    }

    public String getPartnerUid() {
        return partnerUid;
    }

    public void setPartnerUid(String partnerUid) {
        this.partnerUid = partnerUid;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
