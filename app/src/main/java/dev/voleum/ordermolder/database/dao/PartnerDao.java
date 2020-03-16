package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Partner;

@Dao
public interface PartnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Partner... partners);

    @Update
    void updateAll(Partner... partners);

    @Delete
    void deleteAll(Partner... partners);

    @Query("SELECT * FROM partner")
    List<Partner> getAll();

    @Query("SELECT * FROM partner WHERE uid = :uid")
    Partner getByUid(String uid);

    @Query("DELETE FROM partner")
    void deleteAllRecords();
}
