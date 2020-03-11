package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Unit;

@Dao
public interface UnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Unit... units);

    @Update
    void updateAll(Unit... units);

    @Delete
    void deleteAll(Unit... units);

    @Query("SELECT * FROM unit")
    List<Unit> getAll();

    @Query("DELETE FROM unit")
    void deleteAllRecords();
}
