package dev.voleum.ordermolder.Helper;

import android.database.Cursor;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Database.DbPreparerData;
import dev.voleum.ordermolder.MainActivity;

public class XmlHelper {

    private final String ENCODING_UTF8 = "utf-8";

    private final String TAG_DOCUMENT = "Document";
    private final String TAG_ITEM = "Item";
    private final String TAG_ORDER = "Order";
    private final String TAG_CASH_RECEIPT = "CashReceipt";
    private final String TAG_ROW = "Row";

    private final String ATTRIBUTE_UID = "UID";
    private final String ATTRIBUTE_DATE = "Date";
    private final String ATTRIBUTE_COMPANY = "Company";
    private final String ATTRIBUTE_PARTNER = "Partner";
    private final String ATTRIBUTE_WAREHOUSE = "Warehouse";
    private final String ATTRIBUTE_ORDER = "Order";
    private final String ATTRIBUTE_SUM = "Sum";
    private final String ATTRIBUTE_SUM_CREDIT = "SumCredit";
    private final String ATTRIBUTE_POSITION = "Position";
    private final String ATTRIBUTE_GOOD = "Good";
    private final String ATTRIBUTE_QUANTITY = "Quantity";
    private final String ATTRIBUTE_PRICE = "Price";

    private final String NAMESPACE = null;

    public XmlHelper() {
    }

    public boolean parseXml(InputStream input) {

        try {
            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlPullParser xpp = xppf.newPullParser();
            xpp.setInput(input, ENCODING_UTF8);

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        Log.d(MainActivity.LOG_TAG, "Not used type: " + xpp.getEventType()); // FIXME: может их как-то обработать?
                }
            }
            return true;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serializeXml(OutputStream output) {

        try {
            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlSerializer xs = xppf.newSerializer();
            xs.setOutput(output, ENCODING_UTF8);

            xs.startDocument(ENCODING_UTF8, null);

            DbPreparerData preparer = new DbPreparerData();

            // region <Document>
            xs.startTag(NAMESPACE, TAG_DOCUMENT);

            // region <Order>
            xs.startTag(NAMESPACE, TAG_ORDER);

            Cursor ordersCursor = preparer.prepareDoc(DbHelper.TABLE_ORDERS);

            if (ordersCursor.moveToFirst()) {
                int uidIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_UID);
                int dateIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_DATE);
                int companyUidIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
                int partnerUidIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
                int warehouseUidIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
                int sumIndex = ordersCursor.getColumnIndex(DbHelper.COLUMN_SUM);
                do {
                    // region Header
                    xs.startTag(NAMESPACE, TAG_ITEM);
                    xs.attribute(NAMESPACE, ATTRIBUTE_UID, ordersCursor.getString(uidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_DATE, ordersCursor.getString(dateIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_COMPANY, ordersCursor.getString(companyUidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_PARTNER, ordersCursor.getString(partnerUidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_WAREHOUSE, ordersCursor.getString(warehouseUidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_SUM, ordersCursor.getString(sumIndex));
                    // endregion Header

                    // region Table Goods
                    Cursor tablesGoodsCursor = preparer.prepareTable(DbHelper.TABLE_GOODS_TABLE, DbHelper.COLUMN_ORDER_UID, ordersCursor.getString(uidIndex));

                    if (tablesGoodsCursor.moveToFirst()) {
                        int positionIndex = tablesGoodsCursor.getColumnIndex(DbHelper.COLUMN_POSITION);
                        int goodUidIndex = tablesGoodsCursor.getColumnIndex(DbHelper.COLUMN_GOOD_UID);
                        int quantityIndex = tablesGoodsCursor.getColumnIndex(DbHelper.COLUMN_QUANTITY);
                        int priceIndex = tablesGoodsCursor.getColumnIndex(DbHelper.COLUMN_PRICE);
                        int sumGoodIndex = tablesGoodsCursor.getColumnIndex(DbHelper.COLUMN_SUM);
                        do {
                            xs.startTag(NAMESPACE, TAG_ROW);
                            xs.attribute(NAMESPACE, ATTRIBUTE_POSITION, String.valueOf(tablesGoodsCursor.getInt(positionIndex)));
                            xs.attribute(NAMESPACE, ATTRIBUTE_GOOD, tablesGoodsCursor.getString(goodUidIndex));
                            xs.attribute(NAMESPACE, ATTRIBUTE_QUANTITY, String.valueOf(tablesGoodsCursor.getDouble(quantityIndex)));
                            xs.attribute(NAMESPACE, ATTRIBUTE_PRICE, String.valueOf(tablesGoodsCursor.getDouble(priceIndex)));
                            xs.attribute(NAMESPACE, ATTRIBUTE_SUM, String.valueOf(tablesGoodsCursor.getDouble(sumGoodIndex)));
                            xs.endTag(NAMESPACE, TAG_ROW);
                        } while (tablesGoodsCursor.moveToNext());
                        tablesGoodsCursor.close();
                    }
                    // endregion Table Goods

                    xs.endTag(NAMESPACE, TAG_ITEM);
                } while (ordersCursor.moveToNext());
                ordersCursor.close();
            }

            xs.endTag(NAMESPACE, TAG_ORDER);
            // endregion <Order>

            // region <CashReceipt>
            xs.startTag(NAMESPACE, TAG_CASH_RECEIPT);

            Cursor cashReceiptCursor = preparer.prepareDoc(DbHelper.TABLE_CASH_RECEIPTS);

            if (cashReceiptCursor.moveToFirst()) {
                int uidIndex = cashReceiptCursor.getColumnIndex(DbHelper.COLUMN_UID);
                int dateIndex = cashReceiptCursor.getColumnIndex(DbHelper.COLUMN_DATE);
                int companyUidIndex = cashReceiptCursor.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
                int partnerUidIndex = cashReceiptCursor.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
                int sumIndex = cashReceiptCursor.getColumnIndex(DbHelper.COLUMN_SUM);
                do {
                    // region Header
                    xs.startTag(NAMESPACE, TAG_ITEM);
                    xs.attribute(NAMESPACE, ATTRIBUTE_UID, cashReceiptCursor.getString(uidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_DATE, cashReceiptCursor.getString(dateIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_COMPANY, cashReceiptCursor.getString(companyUidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_PARTNER, cashReceiptCursor.getString(partnerUidIndex));
                    xs.attribute(NAMESPACE, ATTRIBUTE_SUM, cashReceiptCursor.getString(sumIndex));
                    // endregion Header

                    // region Table Objects
                    Cursor tablesObjectsCursor = preparer.prepareTable(DbHelper.TABLE_OBJECTS_TABLE, DbHelper.COLUMN_CASH_RECEIPT_UID, cashReceiptCursor.getString(uidIndex));

                    if (tablesObjectsCursor.moveToFirst()) {
                        int positionIndex = tablesObjectsCursor.getColumnIndex(DbHelper.COLUMN_POSITION);
                        int orderUidIndex = tablesObjectsCursor.getColumnIndex(DbHelper.COLUMN_ORDER_UID);
                        int sumCreditIndex = tablesObjectsCursor.getColumnIndex(DbHelper.COLUMN_SUM_CREDIT);
                        do {
                            xs.startTag(NAMESPACE, TAG_ROW);
                            xs.attribute(NAMESPACE, ATTRIBUTE_POSITION, String.valueOf(tablesObjectsCursor.getInt(positionIndex)));
                            xs.attribute(NAMESPACE, ATTRIBUTE_ORDER, tablesObjectsCursor.getString(orderUidIndex));
                            xs.attribute(NAMESPACE, ATTRIBUTE_SUM_CREDIT, String.valueOf(tablesObjectsCursor.getDouble(sumCreditIndex)));
                            xs.endTag(NAMESPACE, TAG_ROW);
                        } while (tablesObjectsCursor.moveToNext());
                        tablesObjectsCursor.close();
                    }
                    // endregion Table Objects

                    xs.endTag(NAMESPACE, TAG_ITEM);
                } while (cashReceiptCursor.moveToNext());
                cashReceiptCursor.close();
            }

            xs.endTag(NAMESPACE, TAG_CASH_RECEIPT);
            // endregion <CashReceipt>

            xs.endTag(NAMESPACE, TAG_DOCUMENT);
            // endregion <Document>

            xs.endDocument();

            xs.flush();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
