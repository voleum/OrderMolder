package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class TableObjects extends Table {

    @Ignore
    public TableObjects(String uid, int position, String objectUid, double sum) {
        super(uid, position);
        this.objUid = objectUid;
        this.objName = objectUid;
        this.sum = sum;
    }

    public TableObjects(String uid, int position, String objUid, String objName, double sum) {
        super(uid, position);
        this.objUid = objUid;
        this.objName = objName;
        this.sum = sum;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getTableObjectsDao().insertAll(this);
        return true;
    }
}
