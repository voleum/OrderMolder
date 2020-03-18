package dev.voleum.ordermolder.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import dev.voleum.ordermolder.models.Obj;

public interface AbstractDao<T extends Obj> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(T... objects);

    @Update
    void updateAll(T... objects);

    @Delete
    void deleteAll(T... objects);
}
