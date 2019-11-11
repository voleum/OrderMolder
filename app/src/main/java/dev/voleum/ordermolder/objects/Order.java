package dev.voleum.ordermolder.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dev.voleum.ordermolder.database.DbHelper;

public class Order extends Document {

    private String warehouseUid;
    private List<TableGoods> tableGoods;

    public Order() {

    }

    public Order(String uid) {
        getOrderFromDb(uid);
    }

    public Order(String uid, String date, String time, String companyUid, String partnerUid, String warehouseUid, double sum) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
    }

    public Order(String uid, String date, String time, String companyUid, String partnerUid, String warehouseUid, double sum, List<TableGoods> tableGoods) {
        super(uid, date, time, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
        this.tableGoods = tableGoods;
    }

    @Override
    public void save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_UID, uid);
        cv.put(DbHelper.COLUMN_DATE, dateTime);
        cv.put(DbHelper.COLUMN_COMPANY_UID, companyUid);
        cv.put(DbHelper.COLUMN_PARTNER_UID, partnerUid);
        cv.put(DbHelper.COLUMN_WAREHOUSE_UID, warehouseUid);
        cv.put(DbHelper.COLUMN_SUM, sum);
        db.insertWithOnConflict(DbHelper.TABLE_ORDERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        String whereClause = DbHelper.COLUMN_GOOD_UID + " = ?";
        String[] whereArgs = { uid };
        db.delete(DbHelper.TABLE_GOODS_TABLE,
                whereClause,
                whereArgs);
        for (TableGoods tg : tableGoods
             ) {
            tg.save(db);
        }
    }

    public String getWarehouseUid() {
        return warehouseUid;
    }

    public void setWarehouseUid(String warehouseUid) {
        this.warehouseUid = warehouseUid;
    }

    public List<TableGoods> getTableGoods() {
        return tableGoods;
    }

    private void getOrderFromDb(String uid) {
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] selectionArgs = { uid };
        String sql = "SELECT *"
                + " FROM " + DbHelper.TABLE_GOODS_TABLE
                + " LEFT JOIN " + DbHelper.TABLE_GOODS
                + " ON " + DbHelper.COLUMN_GOOD_UID + " = " + DbHelper.COLUMN_UID
                + " WHERE " + DbHelper.COLUMN_ORDER_UID + " = ?"
                + " ORDER BY " + DbHelper.COLUMN_POSITION;
        Cursor c = db.rawQuery(sql, selectionArgs);
        int positionClIndex = c.getColumnIndex(DbHelper.COLUMN_POSITION);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
        int quantityClIndex = c.getColumnIndex(DbHelper.COLUMN_QUANTITY);
        int priceClIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            tableGoods = new ArrayList<>();
            do {
                tableGoods.add(new TableGoods(c.getString(uidClIndex),
                        c.getInt(positionClIndex),
                        c.getString(nameClIndex),
                        c.getDouble(quantityClIndex),
                        c.getDouble(priceClIndex),
                        c.getDouble(sumClIndex)));
            } while (c.moveToNext());
        }

        String selection = DbHelper.COLUMN_UID + " = ?";
        c = db.query(DbHelper.TABLE_ORDERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int dateClIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
        int companyClIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
        int partnerClIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
        int warehouseClIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
        sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            uid = c.getString(uidClIndex);
            dateTime = c.getString(dateClIndex);
            companyUid = c.getString(companyClIndex);
            partnerUid = c.getString(partnerClIndex);
            warehouseUid = c.getString(warehouseClIndex);
            sum = c.getDouble(sumClIndex);
        }

        c.close();
        db.close();
    }
}
