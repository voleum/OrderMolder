package dev.voleum.ordermolder.Helper;

import android.os.AsyncTask;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import dev.voleum.ordermolder.R;

public class ExchangeAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private View v;

    public ExchangeAsyncTask(View v) {
        this.v = v;
    }

    @Override
    protected void onPostExecute(Boolean successful) {
        if (successful) {
            Snackbar.make(v, R.string.snackbar_successful, Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.getAppContext(), R.string.snackbar_successful, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ConnectionHelper connection = new ConnectionHelper();
        connection.exchange();
        return true;
    }
}
