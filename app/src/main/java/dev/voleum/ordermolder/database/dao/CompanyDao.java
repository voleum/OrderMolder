package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.Company;

@Dao
public interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Company... companies);

    @Update
    void updateAll(Company... companies);

    @Delete
    void deleteAll(Company... companies);

    @Query("SELECT * FROM company")
    List<Company> getAll();

    @Query("DELETE FROM company")
    void deleteAllRecords();
}
