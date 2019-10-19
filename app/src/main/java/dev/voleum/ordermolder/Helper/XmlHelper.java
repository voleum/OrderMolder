package dev.voleum.ordermolder.Helper;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.voleum.ordermolder.MainActivity;

public class XmlHelper {

    private final String ENCODING_UTF8 = "utf-8";

    private final String TAG_DOCUMENT = "Document";
    private final String TAG_ORDER = "Order";
    private final String TAG_CASH_RECEIPT = "CashReceipt";

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

            // TODO: finish code for creating xml (namespace == null)
            xs.startDocument(ENCODING_UTF8, null);

            // region <Document>
            xs.startTag(null, TAG_DOCUMENT);

            // region <Order>
            xs.startTag(null, TAG_ORDER);

            xs.endTag(null, TAG_ORDER);
            // endregion <Order>

            // region <CashReceipt>
            xs.startTag(null, TAG_CASH_RECEIPT);

            xs.endTag(null, TAG_CASH_RECEIPT);
            // endregion <CashReceipt>

            xs.endTag(null, TAG_DOCUMENT);
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
