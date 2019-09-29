package dev.voleum.ordermolder.Helper;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import dev.voleum.ordermolder.MainActivity;

public class ConnectionHelper {

    private final String FILE_NAME_INPUT = "From1C.xml";
    private final String FILE_NAME_OUTPUT = "To1C.xml";

    // region // TODO: организовать подстановку из настроек
    private String hostname = "ftp.kutuzov-it-ru.1gb.ru";
    private int port = 21;
    private boolean usePassiveMode = true;
    private String username = "w_kutuzov-it-ru_61baf58a";
    private String password = "71ea541e3jkl";
    // endregion

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

            // region Input
            try (InputStream input = ftp.retrieveFileStream(FILE_NAME_INPUT)) {
                if (input == null) {
                    Log.d(MainActivity.LOG_TAG, ftp.getReplyString());
                    return false;
                }
                boolean parsed = XmlHelper.parseXml(input);
                ftp.completePendingCommand();
                if (parsed) ftp.deleteFile(FILE_NAME_INPUT);
            }
            // endregion

            // region Output
            try (OutputStream output = ftp.storeFileStream(FILE_NAME_OUTPUT)) {
                if (output == null) {
                    Log.d(MainActivity.LOG_TAG, ftp.getReplyString());
                    return false;
                }
                XmlHelper.serializeXml(output);
                ftp.completePendingCommand();
            }
            // endregion

            return true;

            // FIXME: отлавливать как IOException или в отдельном блоке?
//        } catch (SocketException e) {
//            e.printStackTrace();
//            return false;
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
