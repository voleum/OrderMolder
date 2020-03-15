package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Order;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Order... orders);

    @Update
    void updateAll(Order... orders);

    @Delete
    void deleteAll(Order... orders);

    @Query("SELECT * FROM `order`")
    List<Order> getAll();

    @Query("DELETE FROM `order`")
    void deleteAllRecords();
}
