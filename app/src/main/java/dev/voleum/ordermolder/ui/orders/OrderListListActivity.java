package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.os.Bundle;

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
        adapter = new OrderListRecyclerViewAdapter(orders);
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
        switch (resultCode) {
            case RESULT_SAVED:
                Snackbar.make(fab, R.string.snackbar_doc_saved, Snackbar.LENGTH_LONG)
                        .show();
                break;
            case RESULT_CREATED:
                Snackbar.make(fab, R.string.snackbar_doc_created, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_undo, (v) -> {
                            // TODO: make undo button
                        })
                        .show();
//                orders.add()
//                adapter.notifyItemInserted();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
