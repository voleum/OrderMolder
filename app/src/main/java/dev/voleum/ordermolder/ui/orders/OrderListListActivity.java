package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import dev.voleum.ordermolder.R;

public class OrderListListActivity extends AppCompatActivity {

    final static String OPEN_FOR_CREATE = "open_for_create";

    private final int REQUEST_CODE = 0;

    int docType;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (resultCode == RESULT_OK) {
            Snackbar.make(fab, R.string.snackbar_doc_created, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action_undo, (v) -> {
                        // TODO: сделать отмену
                    })
                    .show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
