package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Good;

@Dao
public interface GoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Good... goods);

    @Update
    void updateAll(Good... goods);

    @Delete
    void deleteAll(Good... goods);

    @Query("SELECT good.uid, good.name, good.groupUid, good.unitUid, `group`.name AS groupName, unit.name AS unitName FROM good" +
            " LEFT JOIN `group` ON good.groupUid = `group`.uid" +
            " LEFT JOIN unit ON good.unitUid = unit.uid")
    List<Good> getAll();

    @Query("DELETE FROM good")
    void deleteAllRecords();
}
