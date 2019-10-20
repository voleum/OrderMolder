package dev.voleum.ordermolder.Helper;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.R;

public class ConnectionHelper {

    private Resources resources = MainActivity.getRess();
    private SharedPreferences sharedPreferences = MainActivity.getPref();

    private String FILE_NAME_INPUT = resources.getString(R.string.file_name_from);
    private String FILE_NAME_OUTPUT = resources.getString(R.string.file_name_to);

    private String hostname = sharedPreferences.getString("url", "");
    private int port = Integer.parseInt(sharedPreferences.getString("port", resources.getString(R.string.default_port)));
    private boolean usePassiveMode = sharedPreferences.getBoolean("passive", true);
    private String username = sharedPreferences.getString("username", "");
    private String password = sharedPreferences.getString("password", "");

    // TODO: Separate connection, download and upload
    public boolean exchange() {
        FTPClient ftp = new FTPClient();

        try {
            // region Connect
            ftp.connect(hostname, port);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                Log.d(MainActivity.LOG_TAG, ftp.getReplyString());
                return false;
            }
            if (usePassiveMode) ftp.enterLocalPassiveMode();
            ftp.login(username, password);
            // endregion

            XmlHelper xmlHelper = new XmlHelper();

            // region Input
            try (InputStream input = ftp.retrieveFileStream(FILE_NAME_INPUT)) {
                if (input != null) {
                    boolean parsed = xmlHelper.parseXml(input);
                    ftp.completePendingCommand();
                    if (parsed) ftp.deleteFile(FILE_NAME_INPUT);
                }
            }
            // endregion

            // region Output
            try (OutputStream output = ftp.storeFileStream(FILE_NAME_OUTPUT)) {
                if (output == null) {
                    Log.d(MainActivity.LOG_TAG, ftp.getReplyString());
                    return false;
                }
                xmlHelper.serializeXml(output);
                ftp.completePendingCommand();
            }
            // endregion

            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void connect(FTPClient ftp) {

    }

}
