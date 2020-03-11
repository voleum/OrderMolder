package dev.voleum.ordermolder.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import dev.voleum.ordermolder.database.dao.CompanyDao;
import dev.voleum.ordermolder.database.dao.GoodDao;
import dev.voleum.ordermolder.database.dao.GroupDao;
import dev.voleum.ordermolder.database.dao.PartnerDao;
import dev.voleum.ordermolder.database.dao.UnitDao;
import dev.voleum.ordermolder.database.dao.WarehouseDao;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.models.Group;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.Price;
import dev.voleum.ordermolder.models.TableGoods;
import dev.voleum.ordermolder.models.TableObjects;
import dev.voleum.ordermolder.models.Unit;
import dev.voleum.ordermolder.models.Warehouse;

@Database(entities = {
        Company.class,
        Partner.class,
        Warehouse.class,
        Good.class,
        Unit.class,
        Group.class,
//        Order.class,
//        CashReceipt.class,
//        TableGoods.class,
//        TableObjects.class,
//        Price.class
        },
        version = 1,
        exportSchema = false)
public abstract class DbRoom extends RoomDatabase {
    public abstract CompanyDao getCompanyDao();
    public abstract PartnerDao getPartnerDao();
    public abstract WarehouseDao getWarehouseDao();
    public abstract GoodDao getGoodDao();
    public abstract UnitDao getUnitDao();
    public abstract GroupDao getGroupDao();
}
