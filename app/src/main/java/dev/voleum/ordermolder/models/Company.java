package dev.voleum.ordermolder.models;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Company extends EconomicEntity {

    public Company(String uid, String name, String tin) {
        super(uid, name, tin);
    }

    @Override
    public boolean save(DbRoom db) {
        db.getCompanyDao().insertAll(this);
        return true;
    }
}
