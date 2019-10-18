package dev.voleum.ordermolder.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Object.Catalog;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class CatalogListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Catalog> catalogs;

    private int docType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        docType = getIntent().getIntExtra(CatalogActivity.DOC_TYPE, CatalogActivity.TYPE_UNKNOWN);

        setContentView(R.layout.activity_chooser);
        setTitle(R.string.catalog_good_plural);
        recyclerView = findViewById(R.id.recycler_tabdoc);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            recyclerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        catalogs = getCatalogsList();
        CatalogListRecyclerViewAdapter adapter = new CatalogListRecyclerViewAdapter(catalogs);
        adapter.setOnEntryClickListener((v, position) -> {
            Catalog chosenCatalog = catalogs.get(position);
            setResult(RESULT_OK, new Intent()
                    .putExtra("catalog", chosenCatalog));
            finish();
        });
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Catalog> getCatalogsList() {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String table;

        switch (docType) {
            case CatalogActivity.TYPE_COMPANY:
                table = DbHelper.TABLE_COMPANIES;
                break;
            case CatalogActivity.TYPE_PARTNER:
                table = DbHelper.TABLE_PARTNERS;
                break;
            case CatalogActivity.TYPE_GOOD:
                table = DbHelper.TABLE_GOODS;
                break;
            case CatalogActivity.TYPE_UNIT:
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
            // TODO: Finish it
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int unitIndex;
            if (docType == CatalogActivity.TYPE_GOOD) unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT);
            do {
                switch (docType) {
                    case CatalogActivity.TYPE_COMPANY:

                        break;
                    case CatalogActivity.TYPE_PARTNER:

                        break;
                    case CatalogActivity.TYPE_GOOD:
                        catalogs.add(new Good(c.getString(uidIndex), c.getString(nameIndex), null));
                        break;
                    case CatalogActivity.TYPE_UNIT:

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
}
