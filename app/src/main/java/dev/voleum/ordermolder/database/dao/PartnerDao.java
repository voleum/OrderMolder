package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Partner;

@Dao
public interface PartnerDao extends AbstractDao<Partner> {

    @Query("SELECT * FROM partner")
    List<Partner> getAll();

    @Query("SELECT * FROM partner WHERE uid = :uid")
    Partner getByUid(String uid);

    @Query("DELETE FROM partner")
    void deleteAllRecords();
}
