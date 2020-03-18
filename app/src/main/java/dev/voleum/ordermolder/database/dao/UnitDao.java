package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Unit;

@Dao
public interface UnitDao extends BaseDao<Unit> {

    @Query("SELECT * FROM unit")
    List<Unit> getAll();

    @Query("DELETE FROM unit")
    void deleteAllRecords();
}
