package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Good;

@Dao
public interface GoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Good... goods);

    @Update
    void updateAll(Good... goods);

    @Delete
    void deleteAll(Good... goods);

    @Query("SELECT * FROM good")
    List<Good> getAll();

    @Query("DELETE FROM good")
    void deleteAllRecords();
}
