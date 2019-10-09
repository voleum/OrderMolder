package dev.voleum.ordermolder.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

//    private static DbHelper instance = null;
    private static volatile DbHelper instance;

    // region Databases
    /**
     * FIXME: Do all final variable private & not static
     */
    private static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;
    // endregion

    // region Tables
    public static final String TABLE_COMPANIES = "companies";
    public static final String TABLE_PARTNERS = "partners";
    public static final String TABLE_GOODS_GROUPS = "goods_groups";
    public static final String TABLE_GOODS = "goods";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_CASH_RECEIPTS = "cash_receipts";
    public static final String TABLE_UNITS = "units";
    public static final String TABLE_DEBTS = "debts";
    public static final String TABLE_STOCK = "stock";
    public static final String TABLE_PRICE_LIST = "price_list";
    public static final String TABLE_GOODS_TABLE = "goods_table";
    // endregion

    // region Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_PARTNER = "partner";
    public static final String COLUMN_WAREHOUSE = "warehouse";
    public static final String COLUMN_GOOD = "good";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_SUM = "sum";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_OBJECT = "object";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_TIN = "tin"; // Tax Identification Number
    public static final String COLUMN_ORDER_CODE = "order_code";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_GROUP_CODE = "group_code";
    // endregion

    private DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        if (instance != null) throw new RuntimeException("Use getInstance() method to get the single instance of this class."); //CHECK: does it work?
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // region Companies
        db.execSQL("create table "
                + TABLE_COMPANIES + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_TIN + " text, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Partners
        db.execSQL("create table "
                + TABLE_PARTNERS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_TIN + " text, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Units
        db.execSQL("create table "
                + TABLE_UNITS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CODE + " integer, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Goods groups
        db.execSQL("create table "
                + TABLE_GOODS_GROUPS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CODE + " text, "
                + COLUMN_NAME + " text "
                + ")");
        // endregion

        // region Goods
        db.execSQL("create table "
                + TABLE_GOODS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CODE + " text, "
                + COLUMN_NAME + " text, "
                + COLUMN_GROUP_CODE + " text, "
                + COLUMN_UNIT + " integer"
                + ")");
        // endregion

        // region Orders
        db.execSQL("create table "
                + TABLE_ORDERS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CODE + " text, "
                + COLUMN_DATE + " real, "
                + COLUMN_COMPANY + " integer, "
                + COLUMN_PARTNER + " integer, "
                + COLUMN_SUM + " integer"
                + ")");
        // endregion

        // region Cash receipts
        db.execSQL("create table "
                + TABLE_CASH_RECEIPTS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CODE + " text, "
                + COLUMN_DATE + " text, "
                + COLUMN_COMPANY + " integer, "
                + COLUMN_PARTNER + " integer, "
                + COLUMN_COUNT + " integer, "
                + COLUMN_OBJECT + " text"
                + ")");
        // endregion

        // region Debts
        db.execSQL("create table "
                + TABLE_STOCK + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_COMPANY + " integer, "
                + COLUMN_PARTNER + " integer, "
                + COLUMN_SUM + " integer"
                + ")");
        // endregion

        // region Stock
        db.execSQL("create table "
                + TABLE_DEBTS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_GOOD + " integer, "
                + COLUMN_WAREHOUSE + " integer, "
                + COLUMN_COUNT + " integer"
                + ")");
        // endregion

        // region Price list
        db.execSQL("create table "
                + TABLE_PRICE_LIST + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_GOOD + " integer, "
                + COLUMN_PRICE + " integer"
                + ")");
        // endregion

        // region Goods table
        db.execSQL("create table "
                + TABLE_GOODS_TABLE + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_ORDER_CODE + " text, "
                + COLUMN_POSITION + " integer,  "
                + COLUMN_GOOD + " integer, "
                + COLUMN_COUNT + " integer, "
                + COLUMN_PRICE + " integer, "
                + COLUMN_SUM + " integer"
                + ")");
        // endregion

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbHelper.class) {
                instance = new DbHelper(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
            }
        }
        return instance;
    }
}
