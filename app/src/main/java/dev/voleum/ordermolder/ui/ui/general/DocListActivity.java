package dev.voleum.ordermolder.ui.ui.general;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.DocListRecyclerViewAdapter;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Object.CashReceipt;
import dev.voleum.ordermolder.Object.Document;
import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.R;

public class DocListActivity extends AppCompatActivity {

    public final static String IS_CREATING = "is_creating";
    public final static String DOC_ACTIVITY = "doc_activity";
    public final static String DOC = "doc";
    public final static String DOC_TYPE = "doc_type";
    public final static String TYPE_ORDER = "order";
    public final static String TYPE_CASH_RECEIPT = "cash_receipt";

    public final static int REQUEST_CODE = 0;
    public final static int RESULT_SAVED = 2;
    public final static int RESULT_CREATED = 3;

    private RecyclerView recyclerDocs;
    private ArrayList<Document> arrayDocs;
    private DocListRecyclerViewAdapter adapter;
    private String docType;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        docType = getIntent().getStringExtra(DOC_TYPE);
        setContentView(R.layout.activity_doc_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerDocs = findViewById(R.id.recycler_docs);
        recyclerDocs.setHasFixedSize(true);
        recyclerDocs.setLayoutManager(new LinearLayoutManager(this));
        fillDocList();
        adapter = new DocListRecyclerViewAdapter(arrayDocs);
        adapter.setOnEntryCLickListener((v, position) -> {
            Document clickedDoc = arrayDocs.get(position);
            Intent intentOut = new Intent(DocListActivity.this, getIntent().getSerializableExtra(DOC_ACTIVITY).getClass());
            intentOut.putExtra(IS_CREATING, false);
            intentOut.putExtra(DOC, clickedDoc);
            startActivityForResult(intentOut, REQUEST_CODE);
        });
        recyclerDocs.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
                    Intent intentOut = new Intent(DocListActivity.this, getIntent().getSerializableExtra(DOC_ACTIVITY).getClass());
                    intentOut.putExtra(IS_CREATING, true);
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
                position = arrayDocs.indexOf(data.getSerializableExtra(DOC));
                adapter.notifyItemChanged(position + 1);
                break;
            case RESULT_CREATED:
                position = arrayDocs.size();
                arrayDocs.add(position, (Order) data.getSerializableExtra(DOC));
                adapter.notifyItemInserted(position + 1);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fillDocList() {

        arrayDocs = new ArrayList<>();

        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String table;

        switch (docType) {
            case TYPE_ORDER:
                table = DbHelper.TABLE_ORDERS;
            case TYPE_CASH_RECEIPT:
                table = DbHelper.TABLE_CASH_RECEIPTS;
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

        if (c.moveToFirst()) {
            int uidIndex = c.getColumnIndex((DbHelper.COLUMN_UID));
            int dateIndex = c.getColumnIndex((DbHelper.COLUMN_DATE));
            int companyIndex = c.getColumnIndex((DbHelper.COLUMN_COMPANY_UID));
            int partnerIndex = c.getColumnIndex((DbHelper.COLUMN_PARTNER_UID));
            int warehouseIndex = -1;
            if (docType.equals(TYPE_ORDER)) {
                warehouseIndex = c.getColumnIndex((DbHelper.COLUMN_WAREHOUSE_UID));
            }
            int sumIndex = c.getColumnIndex((DbHelper.COLUMN_SUM));
            switch (docType) {
                case TYPE_ORDER:
                    do {
                        arrayDocs.add(new Order(c.getString(uidIndex),
                                c.getString(dateIndex),
                                c.getString(companyIndex),
                                c.getString(partnerIndex),
                                c.getString(warehouseIndex),
                                c.getDouble(sumIndex)));
                    } while (c.moveToNext());
                case TYPE_CASH_RECEIPT:
                    do {
                        arrayDocs.add(new CashReceipt(c.getString(uidIndex),
                                c.getString(dateIndex),
                                c.getString(companyIndex),
                                c.getString(partnerIndex),
                                c.getDouble(sumIndex)));
                    } while (c.moveToNext());
            }

        }

        c.close();
        db.close();
    }
}