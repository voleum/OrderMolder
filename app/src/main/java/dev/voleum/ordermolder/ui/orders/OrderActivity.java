package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

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
}