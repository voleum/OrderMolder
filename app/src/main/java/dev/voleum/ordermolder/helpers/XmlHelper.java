package dev.voleum.ordermolder.helpers;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.models.CashReceipt;
import dev.voleum.ordermolder.models.Company;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.models.Group;
import dev.voleum.ordermolder.models.Obj;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.models.Partner;
import dev.voleum.ordermolder.models.Price;
import dev.voleum.ordermolder.models.Table;
import dev.voleum.ordermolder.models.TableGoods;
import dev.voleum.ordermolder.models.TableObjects;
import dev.voleum.ordermolder.models.Unit;
import dev.voleum.ordermolder.models.Warehouse;

class XmlHelper {

    private final String ENCODING_UTF8 = "utf-8";

    private final String UNKNOWN = "Unknown";

    private final String TAG_CASH_RECEIPT     = "CashReceipt";
    private final String TAG_COMPANY          = "Company";
    private final String TAG_DOCUMENT         = "Document";
    private final String TAG_GOOD             = "Good";
    private final String TAG_GOOD_GROUP       = "GoodGroup";
    private final String TAG_ITEM             = "Item";
    private final String TAG_ORDER            = "Order";
    private final String TAG_PARTNER          = "Partner";
    private final String TAG_PRICE_LIST       = "PriceList";
    private final String TAG_ROW              = "Row";
    private final String TAG_UNIT             = "Unit";
    private final String TAG_WAREHOUSE        = "Warehouse";

    private final String ATTRIBUTE_CODE       = "Code";
    private final String ATTRIBUTE_COMPANY    = "Company";
    private final String ATTRIBUTE_DATE       = "Date";
    private final String ATTRIBUTE_FULL_NAME  = "FullName";
    private final String ATTRIBUTE_GOOD       = "Good";
    private final String ATTRIBUTE_GROUP      = "Group";
    private final String ATTRIBUTE_NAME       = "Name";
    private final String ATTRIBUTE_ORDER      = "Order";
    private final String ATTRIBUTE_PARTNER    = "Partner";
    private final String ATTRIBUTE_POSITION   = "Position";
    private final String ATTRIBUTE_PRICE      = "Price";
    private final String ATTRIBUTE_QUANTITY   = "Quantity";
    private final String ATTRIBUTE_SUM        = "Sum";
    private final String ATTRIBUTE_SUM_CREDIT = "SumCredit";
    private final String ATTRIBUTE_TIN        = "TIN";
    private final String ATTRIBUTE_UID        = "UID";
    private final String ATTRIBUTE_UNIT       = "Unit";
    private final String ATTRIBUTE_WAREHOUSE  = "Warehouse";

    boolean parseXml(InputStream input) {

        try {
            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlPullParser xpp = xppf.newPullParser();
            xpp.setInput(input, ENCODING_UTF8);

            List<Obj> objList     = new ArrayList<>();
            List<Table> tableList = new ArrayList<>();

            String currentTypeElem = UNKNOWN;
            String currentUid = "";

            DbRoom db = OrderMolder.getApplication().getDatabase();

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {

                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        if (xpp.getDepth() == 2) currentTypeElem = xpp.getName();
                        else {
                            switch (xpp.getName()) {
                                case TAG_ITEM:
                                    switch (currentTypeElem) {
                                        case TAG_COMPANY:
                                            objList.add(new Company(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_TIN)));
                                            break;
                                        case TAG_PARTNER:
                                            objList.add(new Partner(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_TIN)));
                                            break;
                                        case TAG_GOOD_GROUP:
                                            objList.add(new Group(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME)));
                                            break;
                                        case TAG_GOOD:
                                            objList.add(new Good(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_GROUP),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_UNIT)));
                                            break;
                                        case TAG_WAREHOUSE:
                                            objList.add(new Warehouse(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME)));
                                            break;
                                        case TAG_UNIT:
                                            objList.add(new Unit(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    Integer.parseInt(xpp.getAttributeValue(null, ATTRIBUTE_CODE)),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_NAME),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_FULL_NAME)));
                                            break;
                                        case TAG_ORDER:
                                            currentUid = xpp.getAttributeValue(null, ATTRIBUTE_UID);
                                            objList.add(new Order(currentUid,
                                                    xpp.getAttributeValue(null, ATTRIBUTE_DATE),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_COMPANY),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_PARTNER),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_WAREHOUSE),
                                                    Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_SUM))));
                                            break;
                                        case TAG_CASH_RECEIPT:
                                            currentUid = xpp.getAttributeValue(null, ATTRIBUTE_UID);
                                            objList.add(new CashReceipt(currentUid,
                                                    xpp.getAttributeValue(null, ATTRIBUTE_DATE),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_COMPANY),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_PARTNER),
                                                    Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_SUM))));
                                            break;
                                        case TAG_PRICE_LIST:
                                            objList.add(new Price(xpp.getAttributeValue(null, ATTRIBUTE_UID),
                                                    Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_PRICE))));
                                            break;
                                        case TAG_ROW:
                                            switch (currentTypeElem) {
                                                case TAG_ORDER:
                                                    objList.add(new TableGoods(currentUid,
                                                            Integer.parseInt(xpp.getAttributeValue(null, ATTRIBUTE_POSITION)),
                                                            xpp.getAttributeValue(null, ATTRIBUTE_GOOD),
                                                            Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_QUANTITY)),
                                                            Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_PRICE)),
                                                            Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_SUM))));
                                                    break;
                                                case TAG_CASH_RECEIPT:
                                                    objList.add(new TableObjects(currentUid,
                                                    Integer.parseInt(xpp.getAttributeValue(null, ATTRIBUTE_POSITION)),
                                                    xpp.getAttributeValue(null, ATTRIBUTE_ORDER),
                                                    Double.parseDouble(xpp.getAttributeValue(null, ATTRIBUTE_SUM_CREDIT))));
                                                    break;
                                            }
                                            break;
                                        default:
                                            continue;
                                    }
                                    break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        int depth = xpp.getDepth();

                        if (depth == 2) {
                            switch (currentTypeElem) {
                                case TAG_COMPANY:
                                    db.getCompanyDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Company[].class));
                                    break;
                                case TAG_PARTNER:
                                    db.getPartnerDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Partner[].class));
                                    break;
                                case TAG_GOOD_GROUP:
                                    db.getGroupDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Group[].class));
                                    break;
                                case TAG_GOOD:
                                    db.getGoodDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Good[].class));
                                    break;
                                case TAG_WAREHOUSE:
                                    db.getWarehouseDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Warehouse[].class));
                                    break;
                                case TAG_UNIT:
                                    db.getUnitDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Unit[].class));
                                    break;
                                case TAG_ORDER:
                                    db.getOrderDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Order[].class));
                                    break;
                                case TAG_CASH_RECEIPT:
                                    db.getCashReceiptDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), CashReceipt[].class));
                                    break;
                                case TAG_PRICE_LIST:
                                    db.getPriceDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), Price[].class));
                                    break;
                            }
                            objList.clear();
                        } else if (depth == 3) {
                            switch (currentTypeElem) {
                                case TAG_ORDER:
                                    db.getTableGoodsDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), TableGoods[].class));
                                    break;
                                case TAG_CASH_RECEIPT:
                                    db.getTableObjectsDao().insertAll(Arrays.copyOf(objList.toArray(), objList.size(), TableObjects[].class));
                                    break;
                            }
                            tableList.clear();
                        }

                    case XmlPullParser.TEXT:
                        break;

                    default:
                        Log.d(OrderMolder.LOG_TAG, "Not used type: " + xpp.getEventType());
                }
                xpp.next();
            }
            return true;
        } catch (XmlPullParserException | IOException e) {
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

    void serializeXml(OutputStream output) {

        try {

            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlSerializer xs = xppf.newSerializer();
            xs.setOutput(output, ENCODING_UTF8);

            DbRoom db = OrderMolder.getApplication().getDatabase();

            xs.startDocument(ENCODING_UTF8, null);

            // region <Document>
            xs.startTag(null, TAG_DOCUMENT);

            // region <Order>
            xs.startTag(null, TAG_ORDER);

            ListIterator<Order> orderListIterator = db.getOrderDao().getAll().listIterator();

                while (orderListIterator.hasNext()) {

                    Order order = orderListIterator.next();

                    // region Header
                    xs.startTag(null, TAG_ITEM);
                    xs.attribute(null, ATTRIBUTE_UID, order.getUid());
                    xs.attribute(null, ATTRIBUTE_DATE, order.getDateTime());
                    xs.attribute(null, ATTRIBUTE_COMPANY, order.getCompanyUid());
                    xs.attribute(null, ATTRIBUTE_PARTNER, order.getPartnerUid());
                    xs.attribute(null, ATTRIBUTE_WAREHOUSE, order.getWarehouseUid());
                    xs.attribute(null, ATTRIBUTE_SUM, String.valueOf(order.getSum()));
                    // endregion Header

                    // region Table Goods
                    ListIterator<TableGoods> tableGoodsListIterator = db.getTableGoodsDao().getByUid(order.getUid()).listIterator();

                        while (tableGoodsListIterator.hasNext()) {

                            TableGoods tableGoods = tableGoodsListIterator.next();

                            xs.startTag(null, TAG_ROW);
                            xs.attribute(null, ATTRIBUTE_POSITION, String.valueOf(tableGoods.getPosition()));
                            xs.attribute(null, ATTRIBUTE_GOOD, tableGoods.getObjUid());
                            xs.attribute(null, ATTRIBUTE_QUANTITY, String.valueOf(tableGoods.getQuantity()));
                            xs.attribute(null, ATTRIBUTE_PRICE, String.valueOf(tableGoods.getPrice()));
                            xs.attribute(null, ATTRIBUTE_SUM, String.valueOf(tableGoods.getSum()));
                            xs.endTag(null, TAG_ROW);
                        }
                    }
                    // endregion Table Goods

                    xs.endTag(null, TAG_ITEM);

            xs.endTag(null, TAG_ORDER);
            // endregion <Order>

            // region <CashReceipt>
            xs.startTag(null, TAG_CASH_RECEIPT);

            ListIterator<CashReceipt> cashReceiptListIterator = db.getCashReceiptDao().getAll().listIterator();

                while (cashReceiptListIterator.hasNext()) {

                    CashReceipt cashReceipt = cashReceiptListIterator.next();

                    // region Header
                    xs.startTag(null, TAG_ITEM);
                    xs.attribute(null, ATTRIBUTE_UID, cashReceipt.getUid());
                    xs.attribute(null, ATTRIBUTE_DATE, cashReceipt.getDateTime());
                    xs.attribute(null, ATTRIBUTE_COMPANY, cashReceipt.getCompanyUid());
                    xs.attribute(null, ATTRIBUTE_PARTNER, cashReceipt.getPartnerUid());
                    xs.attribute(null, ATTRIBUTE_SUM, String.valueOf(cashReceipt.getSum()));
                    // endregion Header

                    // region Table Objects
                    ListIterator<TableObjects> tableObjectsListIterator = db.getTableObjectsDao().getByUid(cashReceipt.getUid()).listIterator();

                        while (tableObjectsListIterator.hasNext()) {

                            TableObjects tableObjects = tableObjectsListIterator.next();

                            xs.startTag(null, TAG_ROW);
                            xs.attribute(null, ATTRIBUTE_POSITION, String.valueOf(tableObjects.getPosition()));
                            xs.attribute(null, ATTRIBUTE_ORDER, tableObjects.getObjUid());
                            xs.attribute(null, ATTRIBUTE_SUM_CREDIT, String.valueOf(tableObjects.getSum()));
                            xs.endTag(null, TAG_ROW);
                        }
                    // endregion Table Objects

                    xs.endTag(null, TAG_ITEM);
                }

            xs.endTag(null, TAG_CASH_RECEIPT);
            // endregion <CashReceipt>

            xs.endTag(null, TAG_DOCUMENT);
            // endregion <Document>

            xs.endDocument();

            xs.flush();
        } catch (XmlPullParserException | IOException e) {
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
