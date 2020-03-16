package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.TableObjects;

@Dao
public interface TableObjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(TableObjects... tableObjects);

    @Update
    void updateAll(TableObjects... tableObjects);

    @Delete
    void deleteAll(TableObjects... tableObjects);

    @Query("SELECT * FROM tableobjects")
    List<TableObjects> getAll();

    @Query("SELECT * FROM tableobjects WHERE uid = :uid")
    List<TableObjects> getByUid(String uid);

    @Query("DELETE FROM tableobjects")
    void deleteAllRecords();
}
