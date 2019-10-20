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
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class GoodsChooser extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Good> goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        setTitle(R.string.catalog_good_plural);
        recyclerView = findViewById(R.id.recycler_tabdoc);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            recyclerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        goods = getGoodList();
        GoodsChooserRecyclerViewAdapter adapter = new GoodsChooserRecyclerViewAdapter(goods);
        adapter.setOnEntryClickListener((v, position) -> {
            Good chosenGood = goods.get(position);
            setResult(RESULT_OK, new Intent()
                    .putExtra("good", chosenGood)
                    .putExtra("quantity", 1.0)
                    .putExtra("price", 1.0)
                    .putExtra("sum", 1.0));
            finish();
        });
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.chooser_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Good> getGoodList() {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DbHelper.TABLE_GOODS,
                null,
                null,
                null,
                null,
                null,
                null);
        goods = new ArrayList<>();

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int groupIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
            do {
                goods.add(new Good(c.getString(uidIndex),
                        c.getString(groupIndex),
                        c.getString(nameIndex),
                        c.getString(unitIndex)));
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
