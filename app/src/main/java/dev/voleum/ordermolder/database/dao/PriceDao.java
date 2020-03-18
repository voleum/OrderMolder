package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Price;

@Dao
public interface PriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Price... prices);

    @Update
    void updateAll(Price... prices);

    @Delete
    void deleteAll(Price... prices);

    @Query("SELECT price.uid, price.price, good.name AS goodName FROM price LEFT JOIN good ON price.uid = good.uid")
    List<Price> getAll();

    @Query("DELETE FROM price")
    void deleteAllRecords();
}
