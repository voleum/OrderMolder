package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Group;

@Dao
public interface GroupDao extends AbstractDao<Group> {

    @Query("SELECT * FROM `group`")
    List<Group> getAll();

    @Query("DELETE FROM `group`")
    void deleteAllRecords();
}
