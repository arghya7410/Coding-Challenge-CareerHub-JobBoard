package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        Properties props = new Properties();
        String connectionString = null;

        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            connectionString = String.format("%s?user=%s&password=%s", url, user, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectionString;
    }
}