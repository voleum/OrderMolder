package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Price;

@Dao
public interface PriceDao extends BaseDao<Price> {

    @Query("SELECT price.uid, price.price, good.name AS goodName FROM price LEFT JOIN good ON price.uid = good.uid")
    List<Price> getAll();

    @Query("DELETE FROM price")
    void deleteAllRecords();
}
