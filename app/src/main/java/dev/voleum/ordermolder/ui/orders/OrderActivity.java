package dev.voleum.ordermolder.ui.orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.voleum.ordermolder.Database.DbAsyncSaveDoc;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.R;

public class OrderActivity extends AppCompatActivity {

    protected ViewPager viewPager;
    protected FloatingActionButton fab;
    protected SectionsPagerAdapter sectionsPagerAdapter;

    private Order orderObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            fab.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) fab.show();
                else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent().getBooleanExtra(OrderListListActivity.OPEN_FOR_CREATE, true)) {
            setTitle(R.string.title_new_order);
        } else {
            // TODO: set title like "Order $number$ $date$"
        }
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    saveDoc();
                    if (getIntent().getBooleanExtra(OrderListListActivity.OPEN_FOR_CREATE, true)) {
                        setResult(OrderListListActivity.RESULT_CREATED, new Intent());
                    } else {
                        setResult(OrderListListActivity.RESULT_SAVED);
                    }
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    break;
                default:
                    dialog.cancel();
            }
        };
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_save_doc)
                .setPositiveButton(R.string.dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.dialog_no, dialogClickListener)
                .setNeutralButton(R.string.dialog_cancel, dialogClickListener)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.doc_save:
                saveDoc();
                Snackbar.make(findViewById(R.id.constraintLayout), R.string.snackbar_doc_saved, Snackbar.LENGTH_LONG)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }

    private void saveDoc() {
        HashMap<String, Object> mainInfo = sectionsPagerAdapter.getMainInfo();
        HashMap<Integer, HashMap<String, Object>> goodsInfo = sectionsPagerAdapter.getGoodsInfo();
        HashMap<String, Map> orderInfo = new HashMap<>();
        orderInfo.put("main_info", mainInfo);
        orderInfo.put("goods_info", goodsInfo);
        DbAsyncSaveDoc dbAsyncSaveDoc = new DbAsyncSaveDoc(this);
        dbAsyncSaveDoc.execute(orderInfo);
        orderObj = new Order("001",
                (String) mainInfo.get("date"),
                new Company((String) mainInfo.get("company_tin")),
                new Partner((String) mainInfo.get("partner_tin")),
                (Double) mainInfo.get("sum")
        );
    }
}