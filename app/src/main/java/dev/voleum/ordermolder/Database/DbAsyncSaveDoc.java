package dev.voleum.ordermolder.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class DbAsyncSaveDoc extends AsyncTask<HashMap<String, Map>, Boolean, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private boolean undoPressed;
    private String title;

    public DbAsyncSaveDoc(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPostExecute(Boolean saved) {
        super.onPostExecute(saved);
        if (!saved) ((Activity) context).setTitle(title);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        Snackbar.make(((Activity) context).findViewById(R.id.view_pager), R.string.snackbar_doc_saved, Snackbar.LENGTH_LONG)
                .setGestureInsetBottomIgnored(true)
                .setAction(R.string.snackbar_action_undo, v -> undoPressed = true)
                .show();
    }

    @Override
    protected Boolean doInBackground(HashMap<String, Map>... orders) {
        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            for (HashMap<String, Map> orderInfo: orders
                 ) {
                HashMap<String, String> mainInfo = (HashMap<String, String>) orderInfo.get("main_info");
                HashMap<Integer, HashMap<String, Object>> goodsInfo = (HashMap<Integer, HashMap<String, Object>>) orderInfo.get("goods_info");

                double sumGood;
                double sumOrder = 0;

                HashMap<String, Object> goodInfo;

                for (int i = 0; i < goodsInfo.size(); i++) {
                    goodInfo = goodsInfo.get(i);
                    sumGood = (Double) goodInfo.get("sum");
                    cv.clear();
                    cv.put(DbHelper.COLUMN_ORDER_UID, mainInfo.get("uid"));
                    cv.put(DbHelper.COLUMN_POSITION, i);
                    cv.put(DbHelper.COLUMN_GOOD_UID, ((Good) goodInfo.get("good")).getUid());
                    cv.put(DbHelper.COLUMN_QUANTITY, (Double) goodInfo.get("quantity"));
                    cv.put(DbHelper.COLUMN_PRICE, (Double) goodInfo.get("price"));
                    cv.put(DbHelper.COLUMN_SUM, sumGood);
                    db.insert(DbHelper.TABLE_GOODS_TABLE, null, cv);
                    sumOrder += sumGood;
                }

                cv.clear();
                cv.put(DbHelper.COLUMN_UID, mainInfo.get("uid"));
                cv.put(DbHelper.COLUMN_DATE, mainInfo.get("date"));
                cv.put(DbHelper.COLUMN_COMPANY_UID, mainInfo.get("company_uid"));
                cv.put(DbHelper.COLUMN_PARTNER_UID, mainInfo.get("partner_uid"));
                cv.put(DbHelper.COLUMN_WAREHOUSE_UID, mainInfo.get("warehouse_uid"));
                cv.put(DbHelper.COLUMN_SUM, sumOrder);
                db.insertWithOnConflict(DbHelper.TABLE_ORDERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

                undoPressed = false;
                publishProgress(true);
                Thread.sleep(2750);
                if (!undoPressed) {
                    db.setTransactionSuccessful();
                    title = mainInfo.get("date").substring(0, 19).replace("-", ".");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            dbHelper.close();
        }
        return null;
    }
}
