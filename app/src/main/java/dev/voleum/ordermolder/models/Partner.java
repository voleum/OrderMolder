package dev.voleum.ordermolder.models;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Partner extends EconomicEntity {

    public Partner(String uid, String name, String tin) {
        super(uid, name, tin);
    }

    @Override
    public boolean save(DbRoom db) {
        db.getPartnerDao().insertAll(this);
        return true;
    }
}
