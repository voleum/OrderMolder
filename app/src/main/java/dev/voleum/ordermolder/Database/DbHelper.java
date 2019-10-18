package dev.voleum.ordermolder.Database;

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
    private static final String DB_NAME = "db_order_molder";
    private static final int DB_VERSION = 1;
    // endregion

    // region Tables
    public static final String TABLE_COMPANIES = "companies";
    public static final String TABLE_PARTNERS = "partners";
    public static final String TABLE_WAREHOUSES = "warehouses";
    public static final String TABLE_GOODS_GROUPS = "goods_groups";
    public static final String TABLE_GOODS = "goods";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_CASH_RECEIPTS = "cash_receipts";
    public static final String TABLE_UNITS = "units";
    public static final String TABLE_DEBTS = "debts";
    public static final String TABLE_STOCK = "stock";
    public static final String TABLE_PRICE_LIST = "price_list";
    public static final String TABLE_GOODS_TABLE = "goods_table";
    public static final String TABLE_OBJECTS_TABLE = "objects_table";
    // endregion

    // region Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_SUM = "sum";
    public static final String COLUMN_SUM_CREDIT = "sum_credit";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_TIN = "tin"; // Tax Identification Number
    public static final String COLUMN_POSITION = "position";
    // UIDs
    public static final String COLUMN_ORDER_UID = "order_uid";
    public static final String COLUMN_CASH_RECEIPT_UID = "cash_receipt_uid";
    public static final String COLUMN_GROUP_UID = "group_uid";
    public static final String COLUMN_UNIT_UID = "unit_uid";
    public static final String COLUMN_COMPANY_UID = "company_uid";
    public static final String COLUMN_PARTNER_UID = "partner_uid";
    public static final String COLUMN_OBJECT_UID = "object_uid";
    public static final String COLUMN_WAREHOUSE_UID = "warehouse_uid";
    public static final String COLUMN_GOOD_UID = "good_uid";
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
                + COLUMN_UID + " text, "
                + COLUMN_TIN + " text, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Partners
        db.execSQL("create table "
                + TABLE_PARTNERS + "("
                + COLUMN_UID + " text, "
                + COLUMN_TIN + " text, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Units
        db.execSQL("create table "
                + TABLE_UNITS + "("
                + COLUMN_UID + " text, "
                + COLUMN_CODE + " integer, "
                + COLUMN_NAME + " text"
                + ")");
        // endregion

        // region Goods groups
        db.execSQL("create table "
                + TABLE_GOODS_GROUPS + "("
                + COLUMN_UID + " text, "
                + COLUMN_NAME + " text "
                + ")");
        // endregion

        // region Goods
        db.execSQL("create table "
                + TABLE_GOODS + "("
                + COLUMN_UID + " text, "
                + COLUMN_NAME + " text, "
                + COLUMN_GROUP_UID + " text, "
                + COLUMN_UNIT_UID + " text"
                + ")");
        // endregion

        // region Warehouses
        db.execSQL("create table "
                + TABLE_WAREHOUSES + "("
                + COLUMN_UID + " text, "
                + COLUMN_NAME + " text "
                + ")");
        // endregion

        // region Orders
        db.execSQL("create table "
                + TABLE_ORDERS + "("
                + COLUMN_UID + " text UNIQUE, "
                + COLUMN_DATE + " real, "
                + COLUMN_COMPANY_UID + " text, "
                + COLUMN_PARTNER_UID + " text, "
                + COLUMN_WAREHOUSE_UID + " text, "
                + COLUMN_SUM + " double"
                + ")");
        // endregion

        // region Cash receipts
        db.execSQL("create table "
                + TABLE_CASH_RECEIPTS + "("
                + COLUMN_UID + " text, "
                + COLUMN_DATE + " real, "
                + COLUMN_COMPANY_UID + " text, "
                + COLUMN_PARTNER_UID + " text, "
                + COLUMN_SUM + " double"
                + ")");
        // endregion

        // region Debts
        db.execSQL("create table "
                + TABLE_STOCK + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_COMPANY_UID + " text, "
                + COLUMN_PARTNER_UID + " text, "
                + COLUMN_SUM + " double"
                + ")");
        // endregion

        // region Stock
        db.execSQL("create table "
                + TABLE_DEBTS + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_GOOD_UID + " text, "
                + COLUMN_WAREHOUSE_UID + " text, "
                + COLUMN_QUANTITY + " double"
                + ")");
        // endregion

        // region Price list
        db.execSQL("create table "
                + TABLE_PRICE_LIST + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_GOOD_UID + " text, "
                + COLUMN_PRICE + " double"
                + ")");
        // endregion

        // region Goods table
        db.execSQL("create table "
                + TABLE_GOODS_TABLE + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_ORDER_UID + " text, "
                + COLUMN_POSITION + " integer,  "
                + COLUMN_GOOD_UID + " text, "
                + COLUMN_QUANTITY + " double, "
                + COLUMN_PRICE + " double, "
                + COLUMN_SUM + " double"
                + ")");
        // endregion

        // region Objects table
        db.execSQL("create table "
                + TABLE_OBJECTS_TABLE + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_CASH_RECEIPT_UID + " text, "
                + COLUMN_POSITION + " integer,  "
                + COLUMN_ORDER_UID + " text, "
                + COLUMN_SUM_CREDIT + " double"
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
