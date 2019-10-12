package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.OrderListRecyclerViewAdapter;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.R;

public class OrderListListActivity extends AppCompatActivity {

    public final static String OPEN_FOR_CREATE = "open_for_create";

    public final static int REQUEST_CODE = 0;
    public final static int RESULT_SAVED = 2;
    public final static int RESULT_CREATED = 3;

    private RecyclerView recyclerOrders;
    private ArrayList<Order> orders;
    private OrderListRecyclerViewAdapter adapter;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerOrders = findViewById(R.id.recycler_orders);
        recyclerOrders.setHasFixedSize(true);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        orders = getOrderList();
        adapter = new OrderListRecyclerViewAdapter(orders);
        adapter.setOnEntryCLickListener((v, position) -> {
            Order clickedOrder = orders.get(position);
            // TODO: open for edit
        });
        recyclerOrders.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
                    Intent intentOut = new Intent(OrderListListActivity.this, OrderActivity.class);
                    intentOut.putExtra(OPEN_FOR_CREATE, true);
                    startActivityForResult(intentOut, REQUEST_CODE);
                }
        );
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_activity_orders);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position;
        switch (resultCode) {
            case RESULT_SAVED:
                Snackbar.make(fab, R.string.snackbar_doc_saved, Snackbar.LENGTH_LONG)
                        .show();
                position = orders.size();
                orders.add(position, (Order) data.getSerializableExtra("order"));
                adapter.notifyItemInserted(position + 1);
                break;
            case RESULT_CREATED:
                Snackbar.make(fab, R.string.snackbar_doc_created, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_undo, (v) -> {
                            // TODO: make undo button
                        })
                        .show();
                position = orders.size();
                orders.add(position, (Order) data.getSerializableExtra("order"));
                adapter.notifyItemInserted(position + 1);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private ArrayList<Order> getOrderList() {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DbHelper.TABLE_ORDERS,
                null,
                null,
                null,
                null,
                null,
                null);
        orders = new ArrayList<>();

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex((DbHelper.COLUMN_UID));
            int dateIndex = c.getColumnIndex((DbHelper.COLUMN_DATE));
            int companyIndex = c.getColumnIndex((DbHelper.COLUMN_COMPANY_UID));
            int partnerIndex = c.getColumnIndex((DbHelper.COLUMN_PARTNER_UID));
            int sumIndex = c.getColumnIndex((DbHelper.COLUMN_SUM));
            do {
                orders.add(new Order(c.getString(uidIndex),
                        c.getString(dateIndex),
                        c.getString(companyIndex),
                        c.getString(partnerIndex),
                        c.getDouble(sumIndex)));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return orders;
    }
}
