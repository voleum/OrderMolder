package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.TableGoods;

@Dao
public interface TableGoodsDao extends BaseDao<TableGoods> {

    @Query("SELECT * FROM tablegoods")
    List<TableGoods> getAll();

    @Query("SELECT * FROM tablegoods WHERE uid = :uid")
    List<TableGoods> getByUid(String uid);

    @Query("DELETE FROM tablegoods WHERE uid = :uid")
    void deleteByUid(String uid);

    @Query("DELETE FROM tablegoods")
    void deleteAllRecords();
}
