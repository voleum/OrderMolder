package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class CashReceipt extends Document {

    @Ignore
    private List<TableObjects> tableObjects;

    @Ignore
    public CashReceipt() {
        setCurrentDate();
        setCurrentTime();
        tableObjects = new ArrayList<>();
        uid = companyUid = partnerUid = "";
        sum = 0;
    }

    @Ignore
    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.tableObjects = new ArrayList<>();
    }

    @Ignore
    public CashReceipt(String uid, String dateTime, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.tableObjects = tableObjects;
    }

    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.tableObjects = new ArrayList<>();
    }

    @Ignore
    public CashReceipt(String uid, String date, String time, String companyUid, String partnerUid, double sum, List<TableObjects> tableObjects) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.tableObjects = tableObjects;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getCashReceiptDao().insertAll(this);
        return true;
    }

    public List<TableObjects> getTableObjects() {
        return tableObjects;
    }

    public void addObject(TableObjects row) {
        tableObjects.add(row);
    }
}
