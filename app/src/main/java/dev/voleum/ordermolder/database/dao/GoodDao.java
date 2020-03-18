package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Good;

@Dao
public interface GoodDao extends BaseDao<Good> {

    @Query("SELECT good.uid, good.name, good.groupUid, good.unitUid, `group`.name AS groupName, unit.name AS unitName FROM good" +
            " LEFT JOIN `group` ON good.groupUid = `group`.uid" +
            " LEFT JOIN unit ON good.unitUid = unit.uid")
    List<Good> getAll();

    @Query("DELETE FROM good")
    void deleteAllRecords();
}
