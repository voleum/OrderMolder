package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.TableObjects;

@Dao
public interface TableObjectsDao extends AbstractDao<TableObjects> {

    @Query("SELECT * FROM tableobjects")
    List<TableObjects> getAll();

    @Query("SELECT * FROM tableobjects WHERE uid = :uid")
    List<TableObjects> getByUid(String uid);

    @Query("DELETE FROM tableobjects")
    void deleteAllRecords();
}
