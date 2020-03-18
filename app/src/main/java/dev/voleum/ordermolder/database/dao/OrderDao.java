package dev.voleum.ordermolder.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import dev.voleum.ordermolder.models.Order;

@Dao
public interface OrderDao extends AbstractDao<Order> {

    @Query("SELECT * FROM `order`")
    List<Order> getAll();

    @Query("SELECT * FROM `order` WHERE uid = :uid")
    Order getByUid(String uid);

    @Query("SELECT * FROM `order` WHERE companyUid = :companyUid AND partnerUID = :partnerUid")
    List<Order> getByCompanyAndPartner(String companyUid, String partnerUid);

    @Query("DELETE FROM `order`")
    void deleteAllRecords();
}
