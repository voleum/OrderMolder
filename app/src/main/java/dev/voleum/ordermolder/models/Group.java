package dev.voleum.ordermolder.models;

import androidx.room.Entity;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Group extends Catalog {

    public Group(String uid, String name){
        super(uid, name);
    }

    @Override
    public boolean save(DbRoom db) {
        db.getGroupDao().insertAll(this);
        return true;
    }
}
