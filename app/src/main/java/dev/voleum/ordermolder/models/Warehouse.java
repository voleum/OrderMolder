package dev.voleum.ordermolder.models;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Warehouse extends Catalog {

    public Warehouse(String uid, String name) {
        super(uid, name);
    }

    @Override
    public boolean save(DbRoom db) {
        db.getWarehouseDao().insertAll(this);
        return true;
    }
}
