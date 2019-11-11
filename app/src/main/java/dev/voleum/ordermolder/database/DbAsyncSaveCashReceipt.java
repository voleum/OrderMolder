package dev.voleum.ordermolder.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import dev.voleum.ordermolder.objects.Order;

public class DbAsyncSaveCashReceipt extends AsyncTask<HashMap<String, Map>, Boolean, Boolean> {
//    private boolean undoPressed;
    private String title;

    public DbAsyncSaveCashReceipt() {
        super();
    }

    @Override
    protected void onPostExecute(Boolean saved) {
        super.onPostExecute(saved);
//        if (!saved) ((Activity) context).setTitle(title);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
//        Snackbar.make(((Activity) context).findViewById(R.id.view_pager), R.string.snackbar_doc_saved, Snackbar.LENGTH_LONG)
//                .setGestureInsetBottomIgnored(true)
////                .setAction(R.string.snackbar_action_undo, v -> undoPressed = true)
//                .show();
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(HashMap<String, Map>... docs) {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            for (HashMap<String, Map> docInfo: docs
                 ) {
                try {
                    HashMap<String, String> mainInfo = (HashMap<String, String>) docInfo.get("main_info");
                    HashMap<Integer, HashMap<String, Object>> objectsInfo = (HashMap<Integer, HashMap<String, Object>>) docInfo.get("objects_info");

                    double sumObject;
                    double sumDoc = 0;

                    HashMap<String, Object> objectInfo;

                    for (int i = 0; i < objectsInfo.size(); i++) {
                        objectInfo = objectsInfo.get(i);
                            sumObject = (Double) objectInfo.get("sum_credit");
                            cv.clear();
                            cv.put(DbHelper.COLUMN_CASH_RECEIPT_UID, mainInfo.get("uid"));
                            cv.put(DbHelper.COLUMN_POSITION, i);
                            cv.put(DbHelper.COLUMN_ORDER_UID, ((Order) objectInfo.get("object")).getUid());
                            cv.put(DbHelper.COLUMN_SUM_CREDIT, (Double) objectInfo.get("sum_credit"));
                            db.insert(DbHelper.TABLE_OBJECTS_TABLE, null, cv);
                            sumDoc += sumObject;
                    }

                    cv.clear();
                    cv.put(DbHelper.COLUMN_UID, mainInfo.get("uid"));
                    cv.put(DbHelper.COLUMN_DATE, mainInfo.get("date"));
                    cv.put(DbHelper.COLUMN_COMPANY_UID, mainInfo.get("company_uid"));
                    cv.put(DbHelper.COLUMN_PARTNER_UID, mainInfo.get("partner_uid"));
                    cv.put(DbHelper.COLUMN_SUM, sumDoc);
                    db.insertWithOnConflict(DbHelper.TABLE_CASH_RECEIPTS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

//                    undoPressed = false;
                    publishProgress(true);
//                Thread.sleep(2750);
//                if (!undoPressed) {
                    db.setTransactionSuccessful();
                    try {
                        title = mainInfo.get("date").substring(0, 19).replace("-", ".");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return true;
//                }
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
