package dev.voleum.ordermolder;

import android.app.Application;
import android.content.Context;

public class OrderMolder extends Application {

    private static OrderMolder instance;

    public OrderMolder() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
