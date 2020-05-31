package dev.voleum.ordermolder;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import dev.voleum.ordermolder.database.DbRoom;

public class OrderMolder extends Application {

    public static final String LOG_TAG = "my_log";
    private static OrderMolder instance;
    private DbRoom database;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        database = Room.databaseBuilder(this, DbRoom.class, "db_order_molder").build();

        boolean firstLaunch = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("first_launch", true);
        if (firstLaunch) fillSharedPrefs();

        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "-1")));
    }

    public DbRoom getDatabase() {
        return database;
    }

    public static Context getContext() {
        return instance;
    }

    public static OrderMolder getApplication() {
        return instance;
    }

    private void fillSharedPrefs() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) editor.putString("theme", "-1");
        else editor.putString("theme", "3");
        editor.putString("port", "21");
        editor.putBoolean("first_launch", false);
        editor.apply();
    }
}
