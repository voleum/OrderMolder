package dev.voleum.ordermolder.helpers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import dev.voleum.ordermolder.R;

public class ExchangeAsyncTask extends AsyncTask<Void, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private View v;

    public ExchangeAsyncTask(View v) {
        this.v = v;
    }

    @Override
    protected void onPostExecute(Boolean successful) {
        int textRes = successful ? R.string.snackbar_successful : R.string.snackbar_unsuccessful;
        Snackbar.make(v, textRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ConnectionHelper connection = new ConnectionHelper();
        return connection.exchange();
    }
}
