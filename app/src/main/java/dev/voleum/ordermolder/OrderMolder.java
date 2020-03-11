package dev.voleum.ordermolder;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.room.Room;

import dev.voleum.ordermolder.database.DbRoom;

public class OrderMolder extends Application {

    private static OrderMolder instance;
    private DbRoom database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DbRoom.class, "db_order_molder").build();
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
}
