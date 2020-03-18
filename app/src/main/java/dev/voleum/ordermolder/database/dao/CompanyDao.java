package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Company;

@Dao
public interface CompanyDao extends BaseDao<Company> {

    @Query("SELECT * FROM company")
    List<Company> getAll();

    @Query("SELECT * FROM company WHERE uid = :uid")
    Company getByUid(String uid);

    @Query("DELETE FROM company")
    void deleteAllRecords();
}
