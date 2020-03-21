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
import io.reactivex.Single;

public class TestDataCreator {

    public static Single<String> createTestData() {
        return Single.create(subscriber -> {

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

            subscriber.onSuccess(OrderMolder.getApplication().getResources().getString(R.string.snackbar_successful));
        });
    }

    private static String getNewUid() {
        return UUID.randomUUID().toString();
    }
}
