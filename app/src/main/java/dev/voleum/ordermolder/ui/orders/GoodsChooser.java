package dev.voleum.ordermolder.ui.orders;

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
import java.util.HashMap;
import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.objects.Good;

public class GoodsChooser extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<HashMap<String, Object>> goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        setTitle(R.string.title_catalogs);
        recyclerView = findViewById(R.id.recycler_tabdoc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        goods = getGoodList();
        GoodsChooserRecyclerViewAdapter adapter = new GoodsChooserRecyclerViewAdapter(goods);
        adapter.setOnEntryClickListener((v, position) -> {
            HashMap<String, Object> chosen = goods.get(position);
            double price = (double) chosen.get("price");
            setResult(RESULT_OK, new Intent()
                    .putExtra("good", (Good) chosen.get("good"))
                    .putExtra("quantity", 1.0)
                    .putExtra("price", price)
                    .putExtra("sum", price));
            finish();
        });
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<HashMap<String, Object>> getGoodList() {
        // TODO: Async
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + DbHelper.COLUMN_UID + ", " +
                DbHelper.COLUMN_GROUP_UID + ", " +
                DbHelper.COLUMN_NAME + ", " +
                DbHelper.COLUMN_UNIT_UID + ", " +
                DbHelper.COLUMN_PRICE +
                " FROM " + DbHelper.TABLE_GOODS +
                " LEFT JOIN " + DbHelper.TABLE_PRICE_LIST +
                " ON " + DbHelper.COLUMN_UID + " = " + DbHelper.COLUMN_GOOD_UID, null);
        goods = new ArrayList<>();
        HashMap<String, Object> values;

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int groupIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
            int priceIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
            do {
                values = new HashMap<>();
                values.put("good", new Good(c.getString(uidIndex),
                        c.getString(groupIndex),
                        c.getString(nameIndex),
                        c.getString(unitIndex)));
                values.put("price", c.getDouble(priceIndex));
                goods.add(values);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return goods;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
