package dev.voleum.ordermolder.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import dev.voleum.ordermolder.Object.Good;

public class DbAsyncSaveDoc extends AsyncTask<HashMap<String, Map>, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public DbAsyncSaveDoc(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(HashMap<String, Map>... orders) {
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
                    // TODO: put the number of order
                    cv.put(DbHelper.COLUMN_ORDER_CODE, "001");
                    cv.put(DbHelper.COLUMN_POSITION, i);
                    cv.put(DbHelper.COLUMN_GOOD, ((Good) goodInfo.get("good")).getNumber());
                    cv.put(DbHelper.COLUMN_QUANTITY, (Double) goodInfo.get("quantity"));
                    cv.put(DbHelper.COLUMN_PRICE, (Double) goodInfo.get("price"));
                    cv.put(DbHelper.COLUMN_SUM, sumGood);
                    db.insert(DbHelper.TABLE_GOODS_TABLE, null, cv);
                    sumOrder += sumGood;
                }

                cv.clear();
                // TODO: put the number
                cv.put(DbHelper.COLUMN_CODE, "001");
                cv.put(DbHelper.COLUMN_DATE, mainInfo.get("date"));
                cv.put(DbHelper.COLUMN_COMPANY, mainInfo.get("company_tin"));
                cv.put(DbHelper.COLUMN_PARTNER, mainInfo.get("partner_tin"));
                cv.put(DbHelper.COLUMN_SUM, sumOrder);
                db.insert(DbHelper.TABLE_ORDERS, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            dbHelper.close();
        }
        return null;
    }
}
