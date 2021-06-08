package com.epam.jwd.pool;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionCreator {

    private static final Properties properties = new Properties();
    private static final String DATABASE_PROPERTIES_FILE_PATH = "." + File.separator + "src" + File.separator + "main" +
            File.separator + "resources" + File.separator + "database.properties";
    private static final String DATABASE_URL_PROPERTY_NAME = "db.url";
    private static final String USER_PROPERTY_NAME = "user";
    private static final String PASSWORD_PROPERTY_NAME = "password";

    private ConnectionCreator() {
    }

    private static class ConnectionCreatorHolder {
        private static final ConnectionCreator instance = new ConnectionCreator();
    }

    public static ConnectionCreator getInstance() {
        return ConnectionCreatorHolder.instance;
    }

    static {
        try {
            properties.load(new FileReader(DATABASE_PROPERTIES_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty(DATABASE_URL_PROPERTY_NAME),
                properties.getProperty(USER_PROPERTY_NAME),
                properties.getProperty(PASSWORD_PROPERTY_NAME));
    }

}