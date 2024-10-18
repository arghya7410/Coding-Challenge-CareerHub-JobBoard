package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    private static final String PROPERTY_FILE_NAME = "database.properties";

    public static Connection getConnection() throws SQLException {
        String connectionString = DBPropertyUtil.getConnectionString(PROPERTY_FILE_NAME);
        return DriverManager.getConnection(connectionString);
    }
}
