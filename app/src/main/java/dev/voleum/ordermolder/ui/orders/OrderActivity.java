package dev.voleum.ordermolder.ui.orders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.voleum.ordermolder.Database.DbAsyncSaveDoc;
import dev.voleum.ordermolder.R;

public class OrderActivity extends AppCompatActivity {

    protected ViewPager viewPager;
    protected FloatingActionButton fab;
    protected SectionsPagerAdapter sectionsPagerAdapter;

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO: show question about saving and return resultCode
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.doc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doc_save:
                HashMap<String, String> mainInfo = sectionsPagerAdapter.getMainInfo();
                HashMap<Integer, HashMap<String, Object>> goodsInfo = sectionsPagerAdapter.getGoodsInfo();
                HashMap<String, Map> orderInfo = new HashMap<>();
                orderInfo.put("main_info", mainInfo);
                orderInfo.put("goods_info", goodsInfo);
                DbAsyncSaveDoc dbAsyncSaveDoc = new DbAsyncSaveDoc(this);
                dbAsyncSaveDoc.execute(orderInfo);
                break;
            default:
                break;
        }
        return true;
    }
}