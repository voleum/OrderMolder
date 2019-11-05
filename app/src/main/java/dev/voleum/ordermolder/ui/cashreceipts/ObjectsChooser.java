package dev.voleum.ordermolder.ui.cashreceipts;

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
import dev.voleum.ordermolder.adapters.ObjectsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Order;

public class ObjectsChooser extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Order> objects;

    private String companyUid;
    private String partnerUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        companyUid = getIntent().getStringExtra(DbHelper.COLUMN_COMPANY_UID);
        partnerUid = getIntent().getStringExtra(DbHelper.COLUMN_PARTNER_UID);
        setTitle(R.string.title_activity_orders);
        recyclerView = findViewById(R.id.recycler_tabdoc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        objects = getObjectList();
        ObjectsChooserRecyclerViewAdapter adapter = new ObjectsChooserRecyclerViewAdapter(objects);
        adapter.setOnEntryClickListener((v, position) -> {
            Order chosenObject = objects.get(position);
            setResult(RESULT_OK, new Intent()
                    .putExtra("object", chosenObject)
                    .putExtra("sum_credit", 1.0));
            finish();
        });
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Order> getObjectList() {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectionArgs = { companyUid, partnerUid };
        Cursor c = db.rawQuery("SELECT *" +
                " FROM " + DbHelper.TABLE_ORDERS +
                " WHERE " + DbHelper.COLUMN_COMPANY_UID + " = ?" +
                " AND " + DbHelper.COLUMN_PARTNER_UID + " = ?",
                selectionArgs);
        objects = new ArrayList<>();

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int dateIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
            int companyIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
            int partnerIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
            int warehouseIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
            int sumIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
            do {
                objects.add(new Order(c.getString(uidIndex),
                        c.getString(dateIndex),
                        c.getString(companyIndex),
                        c.getString(partnerIndex),
                        c.getString(warehouseIndex),
                        c.getDouble(sumIndex)));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return objects;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
