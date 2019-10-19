package dev.voleum.ordermolder.Helper;

import android.os.AsyncTask;
import android.widget.Toast;

import dev.voleum.ordermolder.MainActivity;

public class ExchangeAsyncTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected void onPostExecute(Boolean successful) {
        if (successful) Toast.makeText(MainActivity.getAppContext(), "Successful!", Toast.LENGTH_SHORT);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ConnectionHelper connection = new ConnectionHelper();
        connection.exchange();
        return true;
    }
}
