package dev.voleum.ordermolder.objects;

import androidx.annotation.NonNull;

public abstract class Document extends Obj {

    protected String dateTime;
    protected String companyUid;
    protected String partnerUid;
    protected double sum;

    protected Document() {

    }

    protected Document(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.dateTime = date.replace(".", "-") + time + ".000";
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
    }

    @NonNull
    @Override
    public String toString() {
        String dateStr = dateTime
                .substring(0, 10)
                .replace("-", ".");
        return "Date: " + dateStr + " / Sum: " + sum;
    }

    public String getDate() {
        return dateTime.substring(0, 10).replace("-", ".");
    }

    public void setDate(String date) {
        this.dateTime = date.replace(".", "-") + this.dateTime.substring(11, 19);
    }

    public String getTime() {
        return dateTime.substring(11, 19);
    }

    public void setTime(String time) {
        this.dateTime = this.dateTime.substring(0, 10) + time + ".000";
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
