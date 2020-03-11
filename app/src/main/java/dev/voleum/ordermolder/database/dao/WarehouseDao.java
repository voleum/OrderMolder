package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Warehouse;

@Dao
public interface WarehouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Warehouse... warehouses);

    @Update
    void updateAll(Warehouse... warehouses);

    @Delete
    void deleteAll(Warehouse... warehouses);

    @Query("SELECT * FROM warehouse")
    List<Warehouse> getAll();

    @Query("DELETE FROM warehouse")
    void deleteAllRecords();
}
