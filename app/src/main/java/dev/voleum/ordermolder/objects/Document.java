package dev.voleum.ordermolder.objects;

import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public abstract class Document extends Obj {

    protected String date;
    protected String time;
    protected String companyUid;
    protected String partnerUid;
    protected double sum;

    protected Document() {

    }

    protected Document(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.date = dateTime.substring(0, 10).replace("-", ".");
        this.time = dateTime.substring(11, 19);
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
    }

    protected Document(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.date = date;
        this.time = time;
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
    }

    @NonNull
    @Override
    public String toString() {
        return "Date: " + date + " " + time + " / Sum: " + sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        date = (dd < 10 ? "0" + dd : String.valueOf(dd)) + "." + (mm < 10 ? "0" + mm : String.valueOf(mm)) + "." + yy;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        time = (hh < 10 ? "0" + hh : String.valueOf(hh))
                + ":" + (mm < 10 ? "0" + mm : String.valueOf(mm))
                + ":" + (ss < 10 ? "0" + ss : String.valueOf(ss));
    }
}
