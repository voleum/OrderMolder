// TODO: Delete this class and call in MainActivity

package dev.voleum.ordermolder;

import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;

import androidx.preference.PreferenceManager;

import java.util.UUID;

import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.database.dao.CompanyDao;
import dev.voleum.ordermolder.database.dao.GoodDao;
import dev.voleum.ordermolder.database.dao.GroupDao;
import dev.voleum.ordermolder.database.dao.PartnerDao;
import dev.voleum.ordermolder.database.dao.PriceDao;
import dev.voleum.ordermolder.database.dao.UnitDao;
import dev.voleum.ordermolder.database.dao.WarehouseDao;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.models.Group;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.Price;
import dev.voleum.ordermolder.models.Unit;
import dev.voleum.ordermolder.models.Warehouse;
import io.reactivex.Completable;

public class TestDataCreator {

    public static Completable createTestData() {
        return Completable.create(subscriber -> {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(OrderMolder.getContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("url", "ftp.kutuzov-it-ru.1gb.ru");
            editor.putString("username", "w_kutuzov-it-ru_61baf58a\n");
            editor.putString("password", "71ea541e3jkl");
            editor.putBoolean("passive", true);
            editor.apply();

            DbRoom db = OrderMolder.getApplication().getDatabase();

            CompanyDao companyDao = db.getCompanyDao();
            PartnerDao partnerDao = db.getPartnerDao();
            UnitDao unitDao = db.getUnitDao();
            GroupDao groupDao = db.getGroupDao();
            GoodDao goodDao = db.getGoodDao();
            WarehouseDao warehouseDao = db.getWarehouseDao();
            PriceDao priceDao = db.getPriceDao();

            companyDao.deleteAllRecords();
            partnerDao.deleteAllRecords();
            unitDao.deleteAllRecords();
            groupDao.deleteAllRecords();
            goodDao.deleteAllRecords();
            warehouseDao.deleteAllRecords();
            priceDao.deleteAllRecords();

            Company[] companies = new Company[3];
            for (int i = 0; i < companies.length; i++) {
                companies[i] = new Company(getNewUid(),
                        "Company " + (i + 1),
                        "00000" + (i + 1));
            }
            companyDao.insertAll(companies);

            Partner[] partners = new Partner[10];
            for (int i = 0; i < 10; i++) {
                partners[i] = new Partner(getNewUid(),
                        "Partner " + (i + 1),
                        "00000" + (i + 1));
            }
            partnerDao.insertAll(partners);

            String unitUid = getNewUid();
            for (int i = 0; i < 1; i++) {
                unitDao.insertAll(new Unit(unitUid,
                        737,
                        "шт",
                        "Штука"));
            }

            String[] groupUids = new String[5];
            for (int i = 0; i < groupUids.length; i++) {
                groupUids[i] = getNewUid();
            }

            Group[] groups = new Group[groupUids.length];
            for (int i = 0; i < groupUids.length; i++) {
                groups[i] = new Group(groupUids[i],
                        "Group " + (i + 1));
            }
            groupDao.insertAll(groups);

            String[] goodUids = new String[20];
            for (int i = 0; i < goodUids.length; i++) {
                goodUids[i] = getNewUid();
            }

            Good[] goods = new Good[goodUids.length];
            for (int i = 0; i < goodUids.length; i++) {
                goods[i] = new Good(goodUids[i],
                        groupUids[(i + 1) % 5],
                        "Good " + (i + 1),
                        unitUid);
            }
            goodDao.insertAll(goods);

            Warehouse[] warehouses = new Warehouse[2];
            for (int i = 0; i < 2; i++) {
                warehouses[i] = new Warehouse(getNewUid(),
                        "Warehouse " + (i + 1));
            }
            warehouseDao.insertAll(warehouses);

            android.icu.text.DecimalFormat df = new DecimalFormat("#.##");
            Price[] prices = new Price[goodUids.length];
            for (int i = 0; i < prices.length; i++) {
                prices[i] = new Price(goodUids[i],
                        Double.parseDouble(df.format(i % 2 == 0 ? i * 8.96 + 17.23 : i * 3.09 + 13.51).replace(",", ".")));
            }
            priceDao.insertAll(prices);

            subscriber.onComplete();
        });
    }

//    public static Completable createTestDataOld() {
//        return Completable.create(subscriber -> {
//            SharedPreferences sharedPref = MainActivity.getPref();
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("url", "ftp.kutuzov-it-ru.1gb.ru");
//            editor.putString("username", "w_kutuzov-it-ru_61baf58a\n");
//            editor.putString("password", "71ea541e3jkl");
//            editor.putBoolean("passive", true);
//            editor.apply();
//            DbHelper dbHelper = DbHelper.getInstance();
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            db.delete(DbHelper.TABLE_COMPANIES, null, null);
//            db.delete(DbHelper.TABLE_PARTNERS, null, null);
//            db.delete(DbHelper.TABLE_WAREHOUSES, null, null);
//            db.delete(DbHelper.TABLE_GOODS, null, null);
//            db.delete(DbHelper.TABLE_UNITS, null, null);
//            db.delete(DbHelper.TABLE_GOODS_GROUPS, null, null);
//            db.delete(DbHelper.TABLE_ORDERS, null, null);
//            db.delete(DbHelper.TABLE_GOODS_TABLE, null, null);
//            db.delete(DbHelper.TABLE_CASH_RECEIPTS, null, null);
//            db.delete(DbHelper.TABLE_OBJECTS_TABLE, null, null);
//            db.delete(DbHelper.TABLE_PRICE_LIST, null, null);
//            ContentValues cv = new ContentValues();
//            for (int i = 0; i < 3; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, getNewUid());
//                cv.put(DbHelper.COLUMN_TIN, "00000" + (i + 1));
//                cv.put(DbHelper.COLUMN_NAME, "Company " + (i + 1));
//                db.insert(DbHelper.TABLE_COMPANIES, null, cv);
//            }
//            for (int i = 0; i < 10; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, getNewUid());
//                cv.put(DbHelper.COLUMN_TIN, "00000" + (i + 1));
//                cv.put(DbHelper.COLUMN_NAME, "Partner " + (i + 1));
//                db.insert(DbHelper.TABLE_PARTNERS, null, cv);
//            }
//            String unitUid = getNewUid();
//            for (int i = 0; i < 1; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, unitUid);
//                cv.put(DbHelper.COLUMN_CODE, 737);
//                cv.put(DbHelper.COLUMN_NAME, "шт");
//                cv.put(DbHelper.COLUMN_FULL_NAME, "Штука");
//                db.insert(DbHelper.TABLE_UNITS, null, cv);
//            }
//            String[] groupUids = new String[5];
//            for (int i = 0; i < groupUids.length; i++) {
//                groupUids[i] = getNewUid();
//            }
//            for (int i = 0; i < groupUids.length; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, groupUids[i]);
//                cv.put(DbHelper.COLUMN_NAME, "Group " + (i + 1));
//                db.insert(DbHelper.TABLE_GOODS_GROUPS, null, cv);
//            }
//            String[] goodUids = new String[20];
//            for (int i = 0; i < goodUids.length; i++) {
//                goodUids[i] = getNewUid();
//            }
//            for (int i = 0; i < goodUids.length; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, goodUids[i]);
//                cv.put(DbHelper.COLUMN_NAME, "Good " + (i + 1));
//                cv.put(DbHelper.COLUMN_GROUP_UID, groupUids[(i + 1) % 5]);
//                cv.put(DbHelper.COLUMN_UNIT_UID, unitUid);
//                db.insert(DbHelper.TABLE_GOODS, null, cv);
//            }
//            for (int i = 0; i < 2; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_UID, getNewUid());
//                cv.put(DbHelper.COLUMN_NAME, "Warehouse " + (i + 1));
//                db.insert(DbHelper.TABLE_WAREHOUSES, null, cv);
//            }
//            android.icu.text.DecimalFormat df = new DecimalFormat("#.##");
//            for (int i = 0; i < goodUids.length; i++) {
//                cv.clear();
//                cv.put(DbHelper.COLUMN_GOOD_UID, goodUids[i]);
//                cv.put(DbHelper.COLUMN_PRICE, Double.parseDouble(df.format(i % 2 == 0 ? i * 8.96 + 17.23 : i * 3.09 + 13.51).replace(",", ".")));
//                db.insert(DbHelper.TABLE_PRICE_LIST, null, cv);
//            }
//            dbHelper.close();
//            subscriber.onComplete();
//        });
//    }

    private static String getNewUid() {
        return UUID.randomUUID().toString();
    }
}
