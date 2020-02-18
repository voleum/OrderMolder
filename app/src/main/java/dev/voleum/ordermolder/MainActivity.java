package dev.voleum.ordermolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import dev.voleum.ordermolder.fragments.FragmentCatalogs;
import dev.voleum.ordermolder.fragments.FragmentDocuments;
import dev.voleum.ordermolder.fragments.FragmentMain;
import dev.voleum.ordermolder.fragments.FragmentReports;
import dev.voleum.ordermolder.helpers.Exchanger;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "voleum_log";
    public static final String CHECKED_MENU_ITEM = "checked_menu_item";

    public static final int MENU_ITEM_MAIN = 0;
    public static final int MENU_ITEM_DOCUMENTS = 1;
    public static final int MENU_ITEM_CATALOGS = 2;
    public static final int MENU_ITEM_REPORTS = 3;

    private static Resources resources = null;
    private static SharedPreferences sharedPref = null;

    private int checkedMenuItem = MENU_ITEM_MAIN;

    private FragmentMain fragmentMain;
    private FragmentDocuments fragmentDocuments;
    private FragmentCatalogs fragmentCatalogs;
    private FragmentReports fragmentReports;

    private FragmentTransaction fragmentTransaction;

    private ConstraintLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resources = getResources();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPref.getBoolean("first_launch", true)) fillSharedPrefs();

        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(sharedPref.getString("theme", "-1")));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressLayout = findViewById(R.id.main_progress_layout);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(item -> {
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
        });

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
                TestDataCreator.createTestData()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                                       @Override
                                       public void onSubscribe(Disposable d) {
                                           progressLayout.setAlpha(0.0f);
                                           progressLayout.setVisibility(View.VISIBLE);
                                           progressLayout.animate().alpha(1.0f);
                                           getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                   WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                       }

                                       @Override
                                       public void onComplete() {
                                           Snackbar.make(v, R.string.snackbar_successful, Snackbar.LENGTH_SHORT).show();
                                           progressLayout.setVisibility(View.GONE);
                                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           if (e != null) Log.d(LOG_TAG, e.getMessage());
                                       }
                                   }
                        );
                break;
            case R.id.button_exchange:
                Exchanger.exchange()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                progressLayout.setAlpha(0.0f);
                                progressLayout.setVisibility(View.VISIBLE);
                                progressLayout.animate().alpha(1.0f);
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }

                            @Override
                            public void onSuccess(String s) {
                                Snackbar.make(v, s, Snackbar.LENGTH_SHORT).show();
                                progressLayout.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e != null) Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    private void fillSharedPrefs() {
        SharedPreferences.Editor editor = sharedPref.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) editor.putString("theme", "-1");
        else editor.putString("theme", "3");
        editor.putString("port", "21");
        editor.putBoolean("first_launch", false);
        editor.apply();
    }

    public static Resources getRess() {
        return resources;
    }

    public static SharedPreferences getPref() {
        return sharedPref;
    }
}
