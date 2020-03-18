package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class TableObjects extends Table {

    private String objectUid;
    private String objectName;

    @Ignore
    public TableObjects(String uid, int position, String objectUid, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.objectName = objectUid;
        this.sum = sum;
    }

    public TableObjects(String uid, int position, String objectUid, String objectName, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.objectName = objectName;
        this.sum = sum;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getTableObjectsDao().insertAll(this);
        return true;
    }

    public String getObjectUid() {
        return objectUid;
    }

    public String getObjectName() {
        return objectName;
    }
}
