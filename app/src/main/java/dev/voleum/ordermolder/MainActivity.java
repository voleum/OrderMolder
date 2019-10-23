package dev.voleum.ordermolder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.voleum.ordermolder.Database.DbAsyncTestData;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Fragment.FragmentCatalogs;
import dev.voleum.ordermolder.Fragment.FragmentDocuments;
import dev.voleum.ordermolder.Fragment.FragmentMain;
import dev.voleum.ordermolder.Fragment.FragmentReports;
import dev.voleum.ordermolder.Helper.ExchangeAsyncTask;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "voleum_log";
    public static final String CHECKED_MENU_ITEM = "checked_menu_item";

    public static final int MENU_ITEM_MAIN = 0;
    public static final int MENU_ITEM_DOCUMENTS = 1;
    public static final int MENU_ITEM_CATALOGS = 2;
    public static final int MENU_ITEM_REPORTS = 3;

    private static Context appContext = null;
    private static Resources resources = null;
    private static SharedPreferences sharedPref = null;

    private int checkedMenuItem = MENU_ITEM_MAIN;

    private FragmentMain fragmentMain;
    private FragmentDocuments fragmentDocuments;
    private FragmentCatalogs fragmentCatalogs;
    private FragmentReports fragmentReports;

    private FragmentTransaction fragmentTransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContext = getApplicationContext();
        resources = getResources();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(sharedPref.getString("theme", "-1")));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOnNavigationItemSelectedListener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    if (fragmentMain == null) fragmentMain = new FragmentMain();
                    onNavigationSelectedItem(R.string.title_main, fragmentMain, MENU_ITEM_MAIN);
                    return true;
                case R.id.navigation_documents:
                    if (fragmentDocuments == null) fragmentDocuments = new FragmentDocuments();
                    onNavigationSelectedItem(R.string.title_documents, fragmentDocuments, MENU_ITEM_DOCUMENTS);
                    return true;
                case R.id.navigation_catalogs:
                    if (fragmentCatalogs == null) fragmentCatalogs = new FragmentCatalogs();
                    onNavigationSelectedItem(R.string.title_catalogs, fragmentCatalogs, MENU_ITEM_CATALOGS);
                    return true;
                case R.id.navigation_reports:
                    if (fragmentReports == null) fragmentReports = new FragmentReports();
                    onNavigationSelectedItem(R.string.title_reports, fragmentReports, MENU_ITEM_REPORTS);
                    return true;
            }
            return false;
        };

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentMain = new FragmentMain();
        fragmentDocuments = new FragmentDocuments();
        fragmentCatalogs = new FragmentCatalogs();
        fragmentReports = new FragmentReports();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState != null) checkedMenuItem = savedInstanceState.getInt(CHECKED_MENU_ITEM);

        switch (checkedMenuItem) {
            case MENU_ITEM_MAIN:
                setTitle(R.string.title_main);
                fragmentTransaction.add(R.id.frameLayoutFragment, fragmentMain);
                break;
            case MENU_ITEM_DOCUMENTS:
                setTitle(R.string.title_documents);
                fragmentTransaction.add(R.id.frameLayoutFragment, fragmentDocuments);
                break;
            case MENU_ITEM_CATALOGS:
                setTitle(R.string.title_catalogs);
                fragmentTransaction.add(R.id.frameLayoutFragment, fragmentCatalogs);
                break;
            case MENU_ITEM_REPORTS:
                setTitle(R.string.title_reports);
                fragmentTransaction.add(R.id.frameLayoutFragment, fragmentReports);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHECKED_MENU_ITEM, checkedMenuItem);
    }

    private void onNavigationSelectedItem(@StringRes int title, Fragment fragment, int checkedMenuItem) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, fragment);
        fragmentTransaction.commit();
        setTitle(title);
        this.checkedMenuItem = checkedMenuItem;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create_test_data:
                DbAsyncTestData dbAsyncTestData = new DbAsyncTestData();
                dbAsyncTestData.execute(DbHelper.getInstance(getApplicationContext()));
                break;
            case R.id.button_exchange:
                ExchangeAsyncTask exchangeAsyncTask = new ExchangeAsyncTask(findViewById(R.id.frameLayoutFragment));
                exchangeAsyncTask.execute();
                break;
        }
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static Resources getRess() {
        return resources;
    }

    public static SharedPreferences getPref() {
        return sharedPref;
    }
}
