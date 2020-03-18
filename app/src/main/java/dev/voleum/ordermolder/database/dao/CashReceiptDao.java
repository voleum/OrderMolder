package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.CashReceipt;

@Dao
public interface CashReceiptDao extends BaseDao<CashReceipt> {

    @Query("SELECT * FROM cashreceipt")
    List<CashReceipt> getAll();

    @Query("SELECT * FROM cashreceipt WHERE uid = :uid")
    CashReceipt getByUid(String uid);

    @Query("DELETE FROM cashreceipt")
    void deleteAllRecords();
}
