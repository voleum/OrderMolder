package dev.voleum.ordermolder.viewmodels;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.adapters.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.Unit;
import dev.voleum.ordermolder.objects.Warehouse;

public class CatalogListViewModel extends BaseObservable {

    private List<Catalog> catalogs;
    private CatalogListRecyclerViewAdapter adapter;
    private CatalogTypes catType;

    public CatalogListViewModel(CatalogTypes catType) {
        this.catType = catType;
        initCatalogList();
    }

    @Bindable
    public void setAdapter(CatalogListRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public CatalogListRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<Catalog> catalogs) {
        if (recyclerView.getAdapter() instanceof CatalogListRecyclerViewAdapter) {
            ((CatalogListRecyclerViewAdapter) recyclerView.getAdapter()).setData(catalogs);
        }
    }

    private void initCatalogList() {

        catalogs = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String table;

        switch (catType) {
            case COMPANY:
                table = DbHelper.TABLE_COMPANIES;
                break;
            case PARTNER:
                table = DbHelper.TABLE_PARTNERS;
                break;
            case WAREHOUSE:
                table = DbHelper.TABLE_WAREHOUSES;
                break;
            case GOOD:
                table = DbHelper.TABLE_GOODS;
                break;
            case UNIT:
                table = DbHelper.TABLE_UNITS;
                break;
            default:
                table = "";
        }

        StringBuilder textQuery = new StringBuilder();

        textQuery.append("SELECT " + table + "." + DbHelper.COLUMN_UID + " AS uid" +
                ", " + table + "." + DbHelper.COLUMN_NAME + " AS name");

        if (catType == CatalogTypes.COMPANY || catType == CatalogTypes.PARTNER)
            textQuery.append(", " + table + "." + DbHelper.COLUMN_TIN);
        if (catType == CatalogTypes.GOOD)
            textQuery.append(", " + DbHelper.TABLE_GOODS_GROUPS + "." + DbHelper.COLUMN_UID + " AS groupUid" +
                    ", " + DbHelper.TABLE_UNITS + "." + DbHelper.COLUMN_UID + " AS unitUid" +
                    ", " + DbHelper.TABLE_GOODS_GROUPS + "." + DbHelper.COLUMN_NAME + " AS groupName" +
                    ", " + DbHelper.TABLE_UNITS + "." + DbHelper.COLUMN_NAME + " AS unitName");
        if (catType == CatalogTypes.UNIT)
            textQuery.append(", " + table + "." + DbHelper.COLUMN_CODE +
                    ", " + table + "." + DbHelper.COLUMN_FULL_NAME);

        textQuery.append(" FROM " + table);
        if (catType == CatalogTypes.GOOD)
            textQuery.append(" LEFT JOIN " + DbHelper.TABLE_GOODS_GROUPS +
                    " ON " + table + "." + DbHelper.COLUMN_GROUP_UID + " = " + DbHelper.TABLE_GOODS_GROUPS + "." + DbHelper.COLUMN_UID +
                    " LEFT JOIN " + DbHelper.TABLE_UNITS +
                    " ON " + table + "." + DbHelper.COLUMN_UNIT_UID + " = " + DbHelper.TABLE_UNITS + "." + DbHelper.COLUMN_UID);

        textQuery.append(" ORDER BY " + table + "." + DbHelper.COLUMN_NAME);

        Cursor c = db.rawQuery(textQuery.toString(), null);

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int groupUidIndex = -1;
            int groupNameIndex = -1;
            int tinIndex = -1;
            int unitUidIndex = -1;
            int unitNameIndex = -1;
            int codeIndex = -1;
            int fullNameIndex = -1;
            if (catType == CatalogTypes.COMPANY || catType == CatalogTypes.PARTNER) {
                tinIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            }
            if (catType == CatalogTypes.GOOD) {
                groupUidIndex = c.getColumnIndex("groupUid");
                unitUidIndex = c.getColumnIndex("unitUid");
                groupNameIndex = c.getColumnIndex("groupName");
                unitNameIndex = c.getColumnIndex("unitName");

            }
            if (catType == CatalogTypes.UNIT) {
                codeIndex = c.getColumnIndex(DbHelper.COLUMN_CODE);
                fullNameIndex = c.getColumnIndex(DbHelper.COLUMN_FULL_NAME);
            }
            do {
                switch (catType) {
                    case COMPANY:
                        catalogs.add(new Company(c.getString(uidIndex),
                                c.getString(nameIndex),
                                c.getString(tinIndex)));
                        break;
                    case PARTNER:
                        catalogs.add(new Partner(c.getString(uidIndex),
                                c.getString(nameIndex),
                                c.getString(tinIndex)));
                        break;
                    case GOOD:
                        catalogs.add(new Good(c.getString(uidIndex),
                                c.getString(groupUidIndex),
                                c.getString(nameIndex),
                                c.getString(unitUidIndex),
                                c.getString(groupNameIndex),
                                c.getString(unitNameIndex)));
                        break;
                    case WAREHOUSE:
                        catalogs.add(new Warehouse(c.getString(uidIndex),
                                c.getString(nameIndex)));
                        break;
                    case UNIT:
                        catalogs.add(new Unit(c.getString(uidIndex),
                                c.getInt(codeIndex),
                                c.getString(nameIndex),
                                c.getString(fullNameIndex)));
                        break;
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        adapter = new CatalogListRecyclerViewAdapter(catalogs);
    }
}
