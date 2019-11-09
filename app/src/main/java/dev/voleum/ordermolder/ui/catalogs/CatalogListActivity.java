package dev.voleum.ordermolder.ui.catalogs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.Unit;
import dev.voleum.ordermolder.objects.Warehouse;

public class CatalogListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Catalog> catalogs;

    private CatalogTypes catType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        catType = (CatalogTypes) getIntent().getSerializableExtra(CatalogActivity.CAT_TYPE);

        CatalogListRecyclerViewAdapter.OnEntryClickListener onEntryClickListener = (v, position) -> {
            Catalog clickedCat = catalogs.get(position);
            Intent intentOut = new Intent(CatalogListActivity.this, CatalogActivity.class)
                    .putExtra(CatalogActivity.CAT_TYPE, catType)
                    .putExtra(CatalogActivity.CAT, clickedCat);
            startActivity(intentOut);
        };

        recyclerView = findViewById(R.id.recycler_objs);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        catalogs = getCatalogsList();
        CatalogListRecyclerViewAdapter adapter = new CatalogListRecyclerViewAdapter(catalogs);
        adapter.setOnEntryClickListener(onEntryClickListener);
        recyclerView.setAdapter(adapter);

        setTitleDependOnType();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Catalog> getCatalogsList() {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
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

        Cursor c = db.query(table,
                null,
                null,
                null,
                null,
                null,
                null);
        catalogs = new ArrayList<>();

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int groupIndex = -1;
            int tinIndex = -1;
            int unitIndex = -1;
            int codeIndex = -1;
            int fullNameIndex = -1;
            if (catType == CatalogTypes.COMPANY || catType == CatalogTypes.PARTNER) tinIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            if (catType == CatalogTypes.GOOD) {
                groupIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
                unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
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
                                c.getString(groupIndex),
                                c.getString(nameIndex),
                                c.getString(unitIndex)));
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

        return catalogs;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTitleDependOnType() {
        switch (catType) {
            case COMPANY:
                setTitle(R.string.title_activity_companies);
                break;
            case PARTNER:
                setTitle(R.string.title_activity_partners);
                break;
            case GOOD:
                setTitle(R.string.title_activity_goods);
                break;
            case WAREHOUSE:
                setTitle(R.string.title_activity_warehouses);
                break;
            case UNIT:
                setTitle(R.string.title_activity_units);
                break;
        }
    }
}
