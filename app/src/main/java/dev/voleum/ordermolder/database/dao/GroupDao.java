package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Group;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group... groups);

    @Update
    void updateAll(Group... groups);

    @Delete
    void deleteAll(Group... groups);

    @Query("SELECT * FROM `Group`")
    List<Group> getAll();

    @Query("DELETE FROM `group`")
    void deleteAllRecords();
}
