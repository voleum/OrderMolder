package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Warehouse;

@Dao
public interface WarehouseDao extends BaseDao<Warehouse> {

    @Query("SELECT * FROM warehouse")
    List<Warehouse> getAll();

    @Query("SELECT * FROM warehouse WHERE uid = :uid")
    Warehouse getByUid(String uid);

    @Query("DELETE FROM warehouse")
    void deleteAllRecords();
}
