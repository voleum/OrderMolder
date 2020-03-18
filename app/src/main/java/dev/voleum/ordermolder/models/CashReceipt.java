package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class CashReceipt extends Document<TableObjects> {

    @Ignore
    public CashReceipt() {
        super();
    }

    @Ignore
    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid, dateTime, companyUid, partnerUid, sum);
    }

    @Ignore
    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.table = tableObjects;
    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
    }

    @Ignore
    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.table = tableObjects;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getCashReceiptDao().insertAll(this);
        return true;
    }
}
