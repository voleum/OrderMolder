package dev.voleum.ordermolder.Object;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.voleum.ordermolder.Database.DbHelper;

public class Good extends Catalog {

    private String groupUid;
    private String unitUid;

    public Good(String uid, String groupUid, String name, String unitUid) {
        super(uid, name);
        this.groupUid = groupUid;
        this.unitUid = unitUid;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_GROUP_UID, groupUid);
        cv.put(DbHelper.COLUMN_NAME, name);
        cv.put(DbHelper.COLUMN_UNIT_UID, unitUid);
        db.insertWithOnConflict(DbHelper.TABLE_GOODS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getUnitUid() {
        return unitUid;
    }

    public void setUnitUid(String unitUid) {
        this.unitUid = unitUid;
    }
}
