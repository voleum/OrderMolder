package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.TableGoods;

@Dao
public interface TableGoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(TableGoods... tableGoods);

    @Update
    void updateAll(TableGoods... orders);

    @Delete
    void deleteAll(TableGoods... orders);

    @Query("SELECT * FROM tablegoods")
    List<TableGoods> getAll();

    @Query("DELETE FROM tablegoods")
    void deleteAllRecords();
}
