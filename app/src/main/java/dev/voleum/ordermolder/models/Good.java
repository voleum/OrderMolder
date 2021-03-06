package dev.voleum.ordermolder.models;

import androidx.room.Entity;
import androidx.room.Ignore;

import dev.voleum.ordermolder.database.DbRoom;

@Entity
public class Good extends Catalog {

    private String groupUid;
    private String unitUid;
    private String groupName;
    private String unitName;

    @Ignore
    public Good(String uid, String groupUid, String name, String unitUid) {
        super(uid, name);
        this.groupUid = groupUid;
        this.unitUid = unitUid;
        this.groupName = groupUid;
        this.unitName = unitUid;
    }

    public Good(String uid, String groupUid, String name, String unitUid, String groupName, String unitName) {
        super(uid, name);
        this.groupUid = groupUid;
        this.unitUid = unitUid;
        this.groupName = groupName;
        this.unitName = unitName;
    }

    @Override
    public boolean save(DbRoom db) {
        db.getGoodDao().insertAll(this);
        return true;
    }

    public String getGroupUid() {
        return groupUid;
    }

    public void setGroupUid(String groupUid) {
        this.groupUid = groupUid;
    }

    public String getUnitUid() {
        return unitUid;
    }

    public void setUnitUid(String unitUid) {
        this.unitUid = unitUid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
