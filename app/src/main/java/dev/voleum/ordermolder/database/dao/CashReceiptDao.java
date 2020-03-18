package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.voleum.ordermolder.models.CashReceipt;

@Dao
public interface CashReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CashReceipt... cashReceipts);

    @Update
    void updateAll(CashReceipt... cashReceipts);

    @Delete
    void deleteAll(CashReceipt... cashReceipts);

    @Query("SELECT * FROM cashreceipt")
    List<CashReceipt> getAll();

    @Query("SELECT * FROM cashreceipt WHERE uid = :uid")
    CashReceipt getByUid(String uid);

    @Query("DELETE FROM cashreceipt")
    void deleteAllRecords();
}
