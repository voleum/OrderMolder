package dev.voleum.ordermolder.ui.orders;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.Helper.DbHelper;
import dev.voleum.ordermolder.Helper.MyRecyclerViewAdapter;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class GoodsChooser extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Good> goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_chooser);
        setTitle(R.string.catalog_good_plural);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        goods = getGoodList();
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, goods);
        adapter.setOnEntryClickListener(new MyRecyclerViewAdapter.OnEntryClickListener() {
                    @Override
                    public void onEntryClick(View v, int position) {
//                        Toast.makeText(GoodsChooser.this.getApplicationContext(), "Clicked " + position, Toast.LENGTH_LONG)
//                                .show();
                        Good choosedGood = goods.get(position);
                        Toast.makeText(getApplicationContext(), choosedGood.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Good> getGoodList() {
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DbHelper.TABLE_GOODS, null, null, null, null, null, null);
        goods = new ArrayList<>();

        if (c.moveToFirst()) {
            int numberIndex = c.getColumnIndex(DbHelper.COLUMN_CODE);
            int nameIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int unitIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT);
            do {
                goods.add(new Good(c.getString(numberIndex), c.getString(nameIndex), null));
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
