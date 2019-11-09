package dev.voleum.ordermolder.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;

import dev.voleum.ordermolder.objects.Obj;

public class DbAsyncSaveObj extends AsyncTask<Obj, Void, Boolean> {

    public DbAsyncSaveObj() {
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(Obj... objs) {
        GroupSaver groupSaver = new GroupSaver(new ArrayList<>(Arrays.asList(objs)));
        groupSaver.save();
        return null;
    }
}
