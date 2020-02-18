package dev.voleum.ordermolder.helpers;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.R;

class ConnectionHelper {

    private Resources resources = MainActivity.getRess();
    private SharedPreferences sharedPreferences = MainActivity.getPref();

    private String FILE_NAME_INPUT = resources.getString(R.string.file_name_from);
    private String FILE_NAME_OUTPUT = resources.getString(R.string.file_name_to);

    private String hostname = sharedPreferences.getString("url", "");
    private int port = Integer.parseInt(sharedPreferences.getString("port", resources.getString(R.string.default_port)));
    private boolean usePassiveMode = sharedPreferences.getBoolean("passive", true);
    private String username = sharedPreferences.getString("username", "");
    private String password = sharedPreferences.getString("password", "");

    String exchange() {

        if (hostname.isEmpty()) return MainActivity.getRess().getString(R.string.snackbar_empty_hostname);
        if (username.isEmpty()) return MainActivity.getRess().getString(R.string.snackbar_empty_username);
        if (password.isEmpty()) return MainActivity.getRess().getString(R.string.snackbar_empty_password);

        FTPClient ftp = new FTPClient();

        try {
            // region Connect
            ftp.connect(hostname, port);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                return ftp.getReplyString();
            }
            if (usePassiveMode) ftp.enterLocalPassiveMode();
            ftp.login(username, password);
            // endregion

            XmlHelper xmlHelper = new XmlHelper();

            // region Output
            try (OutputStream output = ftp.storeFileStream(FILE_NAME_OUTPUT)) {
                if (output == null) {
                    return MainActivity.getRess().getString(R.string.snackbar_couldnt_send_data);
                }
                xmlHelper.serializeXml(output);
                ftp.completePendingCommand();
            }
            // endregion

            // region Input
            try (InputStream input = ftp.retrieveFileStream(FILE_NAME_INPUT)) {
                if (input == null) {
                    return MainActivity.getRess().getString(R.string.snackbar_couldnt_receive_data);
                }
                boolean parsed = xmlHelper.parseXml(input);
                ftp.completePendingCommand();
                if (parsed) ftp.deleteFile(FILE_NAME_INPUT);
            }
            // endregion

            return MainActivity.getRess().getString(R.string.snackbar_successful);
        } catch (SocketException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
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
}
