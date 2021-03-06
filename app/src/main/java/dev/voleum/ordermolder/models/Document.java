package dev.voleum.ordermolder.models;

import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

public abstract class Document<T extends Table> extends Obj {

    protected String date;
    protected String time;
    protected String companyUid;
    protected String partnerUid;
    protected double sum;

    @Ignore
    protected List<T> table;

    protected Document() {
        setCurrentDate();
        setCurrentTime();
        table = new ArrayList<>();
        uid = companyUid = partnerUid = "";
        sum = 0;
    }

    protected Document(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.date = dateTime.substring(0, 10).replace('-', '.');
        this.time = dateTime.substring(11, 19);
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
        this.table = new ArrayList<>();
    }

    protected Document(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid);
        this.date = date;
        this.time = time;
        this.companyUid = companyUid;
        this.partnerUid = partnerUid;
        this.sum = sum;
        this.table = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
//        return "Date: " + date + " " + time + " / Sum: " + moneyDecimalFormat.format(sum) + " \u20BD";
        return "Date: " + date + " " + time;
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

    public String getDateTime() {
        return date.replace('.', '-') + " " + time + ".000";
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

    public void setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        date = (dd < 10 ? "0" + dd : String.valueOf(dd)) + "." + (mm < 10 ? "0" + mm : String.valueOf(mm)) + "." + yy;
    }

    public void setCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        time = (hh < 10 ? "0" + hh : String.valueOf(hh))
                + ":" + (mm < 10 ? "0" + mm : String.valueOf(mm))
                + ":" + (ss < 10 ? "0" + ss : String.valueOf(ss));
    }

    public List<T> getTable() {
        return table;
    }

    public void addRow(T row) {
        table.add(row);
    }
}
