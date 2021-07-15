package com.epam.jwd.pool;

import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolManager implements ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolManager.class);
    private static final Properties properties = new Properties();
    private static final String FAILED_TO_OPEN_CONNECTION_MSG = "failed to open connection";
    private static final String DRIVER_REGISTRATION_FAILED_MSG = "driver registration failed";
    private static final String DATABASE_PROPERTIES_FILE_NAME = "database.properties";
    private static final String DATABASE_URL;
    private static final String DATABASE_USERNAME;
    private static final String DATABASE_PASSWORD;
    private static final int NUMBER_OF_ADDED_CONNECTIONS;
    private static final int INIT_POOL_SIZE;
    private static final int MAX_POOL_SIZE;

    static {
        try (InputStream propertyFile = ConnectionPoolManager.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_FILE_NAME)) {
            properties.load(propertyFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        DATABASE_URL = properties.getProperty("db.url");
        DATABASE_USERNAME = properties.getProperty("user");
        DATABASE_PASSWORD = properties.getProperty("password");
        NUMBER_OF_ADDED_CONNECTIONS = Integer.parseInt(properties.getProperty("numberOfAddedConnections"));
        INIT_POOL_SIZE = Integer.parseInt(properties.getProperty("initPoolSize"));
        MAX_POOL_SIZE = Integer.parseInt(properties.getProperty("maxPoolSize"));
    }

    private final AtomicBoolean initialized;
    private final AtomicInteger connectionsOpened;
    private final LinkedBlockingQueue<ProxyConnection> idle;
    private final LinkedBlockingQueue<ProxyConnection> busy;
    private final Lock init;
    private final Lock destroy;

    private static volatile ConnectionPoolManager instance;

    private ConnectionPoolManager() {
        initialized = new AtomicBoolean(false);
        connectionsOpened = new AtomicInteger(0);
        idle = new LinkedBlockingQueue<>();
        busy = new LinkedBlockingQueue<>();
        init = new ReentrantLock();
        destroy = new ReentrantLock();
    }

    public static ConnectionPoolManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionPoolManager.class) {
                if (instance == null) {
                    instance = new ConnectionPoolManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void init() throws CouldNotInitializeConnectionPoolException {
        init.lock();
        try {
            if (initialized.compareAndSet(false, true)) {
                registerDrivers();
                try {
                    for (int i = 0; i < INIT_POOL_SIZE; i++) {
                        final Connection connection = DriverManager.getConnection(DATABASE_URL,
                                DATABASE_USERNAME,
                                DATABASE_PASSWORD);
                        final ProxyConnection proxyConnection = new ProxyConnection(connection);
                        idle.add(proxyConnection);
                    }
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                    initialized.set(false);
                    throw new CouldNotInitializeConnectionPoolException(FAILED_TO_OPEN_CONNECTION_MSG, e);
                }
                connectionsOpened.set(INIT_POOL_SIZE);
            }
        } finally {
            init.unlock();
        }
    }

    @Override
    public Connection takeConnection() throws SQLException, InterruptedException {
        final int currentOpenedConnections = connectionsOpened.get();
        ProxyConnection connection = null;

        try {
            connection = idle.take();
            busy.add(connection);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }

        if (idle.size() <= currentOpenedConnections * 0.25 && currentOpenedConnections < MAX_POOL_SIZE - 4) {
            addConnectionToPool();
        }

        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            if (connection instanceof ProxyConnection) {
                try {
                    idle.put((ProxyConnection) connection);
                    busy.remove(connection);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void destroy() throws InterruptedException {
        destroy.lock();
        try {
            if (initialized.compareAndSet(true, false)) {
                for (ProxyConnection connection : idle) {
                    try {
                        connection.realClose();
                    } catch (SQLException e) {
                        LOGGER.error(e.getMessage());
                    }
                }

                for (ProxyConnection connection : busy) {
                    try {
                        connection.realClose();
                    } catch (SQLException e) {
                        LOGGER.error(e.getMessage());
                    }
                }

                deregisterDrivers();
            }
        } finally {
            destroy.unlock();
        }
    }

    private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            initialized.set(false);
            throw new CouldNotInitializeConnectionPoolException(DRIVER_REGISTRATION_FAILED_MSG, e);
        }
    }

    private void deregisterDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void addConnectionToPool() {
        try {
            for (int i = 0; i < NUMBER_OF_ADDED_CONNECTIONS; i++) {
                final Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                final ProxyConnection proxyConnection = new ProxyConnection(connection);
                idle.add(proxyConnection);
            }

            final int currentOpenedConnections = connectionsOpened.get() + NUMBER_OF_ADDED_CONNECTIONS;
            connectionsOpened.set(currentOpenedConnections);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}