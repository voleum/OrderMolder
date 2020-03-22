package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Order extends Document<TableGoods> {

    private String warehouseUid;

    @Ignore
    public Order() {
        super();
        warehouseUid = "";
    }

    @Ignore
    public Order(String uid, String dateTime, String companyUid, String partnerUid, String warehouseUid, double sum) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
    }

    @Ignore
    public Order(String uid, String dateTime, String companyUid, String partnerUid, String warehouseUid, double sum, List<TableGoods> tableGoods) {
        super(uid, dateTime, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
        this.table = tableGoods;
    }

    public Order(String uid, String date, String time, String companyUid, String partnerUid, String warehouseUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
    }

    @Ignore
    public Order(String uid, String date, String time, String companyUid, String partnerUid, String warehouseUid, double sum, List<TableGoods> tableGoods) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
        this.table = tableGoods;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getOrderDao().insertAll(this);
        return true;
    }

    public String getWarehouseUid() {
        return warehouseUid;
    }

    public void setWarehouseUid(String warehouseUid) {
        this.warehouseUid = warehouseUid;
    }
}
