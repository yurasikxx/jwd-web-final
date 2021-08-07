package com.epam.jwd.pool;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
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

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * Class {@code ConnectionPoolManager} manages the connection pool
 * and implements all {@code ConnectionPool} methods.
 *
 * @see ConnectionPool
 * @see ProxyConnection
 */
public class ConnectionPoolManager implements ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolManager.class);

    private static final String FAILED_TO_INITIALIZE_CONNECTION_POOL_MSG = "Failed to initialize connection pool";
    private static final String FAILED_TO_DESTROY_CONNECTION_POOL_MSG = "Failed to destroy connection pool";
    private static final String DRIVER_REGISTRATION_FAILED_MSG = "Driver registration failed";
    private static final String PROPERTIES_WERE_NOT_LOADED_MSG = "Properties weren't loaded";
    private static final String POOL_WAS_INITIALIZED_BY_INITIAL_SIZE_MSG = "Pool was initialized by initial size";
    private static final String POOL_WAS_NOT_INITIALIZED_MSG = "Pool wasn't initialized";
    private static final String CONNECTION_WAS_TAKEN_MSG = "Connection was taken";
    private static final String CONNECTION_WAS_NOT_TAKEN_MSG = "Connection wasn't taken";
    private static final String POOL_HAS_BEEN_EXPANDED_MSG = "Pool has been expanded";
    private static final String CONNECTION_WAS_RELEASED_MSG = "Connection was released";
    private static final String CONNECTION_WAS_NOT_RELEASED_MSG = "Connection wasn't released";
    private static final String CONNECTION_WAS_CLOSED_MSG = "Connection was closed";
    private static final String CONNECTION_WAS_NOT_CLOSED_MSG = "Connection wasn't closed";
    private static final String DRIVERS_WERE_NOT_REGISTERED_MSG = "Drivers weren't registered";
    private static final String DRIVERS_WERE_REGISTERED_MSG = "Drivers were registered";
    private static final String DRIVERS_WERE_DEREGISTERED_MSG = "Drivers were deregistered";
    private static final String DRIVERS_WERE_NOT_DEREGISTERED_MSG = "Drivers weren't deregistered";
    private static final String POOL_HAS_NOT_BEEN_EXPANDED_MSG = "Pool hasn't been expanded";
    private static final String PROPERTIES_WERE_INITIALIZED_MSG = "Properties were initialized";
    private static final String PROPERTIES_WERE_LOADED_MSG = "Properties were loaded";
    private static final double CURRENT_OPENED_CONNECTIONS_EXPANDING_VALUE = 0.25;

    private static final Properties properties = new Properties();
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
            LOGGER.info(PROPERTIES_WERE_LOADED_MSG);
        } catch (IOException e) {
            LOGGER.error(PROPERTIES_WERE_NOT_LOADED_MSG);
        }

        DATABASE_URL = properties.getProperty("db.url");
        DATABASE_USERNAME = properties.getProperty("user");
        DATABASE_PASSWORD = properties.getProperty("password");
        NUMBER_OF_ADDED_CONNECTIONS = Integer.parseInt(properties.getProperty("numberOfAddedConnections"));
        INIT_POOL_SIZE = Integer.parseInt(properties.getProperty("initPoolSize"));
        MAX_POOL_SIZE = Integer.parseInt(properties.getProperty("maxPoolSize"));

        LOGGER.info(PROPERTIES_WERE_INITIALIZED_MSG);
    }

    private final AtomicBoolean initialized;
    private final AtomicInteger connectionsOpened;
    private final LinkedBlockingQueue<ProxyConnection> idle;
    private final LinkedBlockingQueue<ProxyConnection> busy;
    private final Lock lock;

    private static volatile ConnectionPoolManager instance;

    private ConnectionPoolManager() {
        initialized = new AtomicBoolean(false);
        connectionsOpened = new AtomicInteger(MIN_INDEX_VALUE);
        idle = new LinkedBlockingQueue<>();
        busy = new LinkedBlockingQueue<>();
        lock = new ReentrantLock();
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
        lock.lock();
        try {
            if (initialized.compareAndSet(false, true)) {
                registerDrivers();

                try {
                    initializePoolByInitSize();
                    LOGGER.info(POOL_WAS_INITIALIZED_BY_INITIAL_SIZE_MSG);
                } catch (SQLException e) {
                    LOGGER.error(POOL_WAS_NOT_INITIALIZED_MSG);
                    initialized.set(false);
                    throw new CouldNotInitializeConnectionPoolException(FAILED_TO_INITIALIZE_CONNECTION_POOL_MSG, e);
                }

                connectionsOpened.set(INIT_POOL_SIZE);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Connection takeConnection() throws SQLException, InterruptedException {
        final int currentOpenedConnections = connectionsOpened.get();
        ProxyConnection connection = null;

        try {
            connection = idle.take();
            busy.add(connection);
            LOGGER.info(CONNECTION_WAS_TAKEN_MSG);
        } catch (InterruptedException e) {
            LOGGER.error(CONNECTION_WAS_NOT_TAKEN_MSG);
        }

        if (idle.size() <= currentOpenedConnections * CURRENT_OPENED_CONNECTIONS_EXPANDING_VALUE
                && currentOpenedConnections < MAX_POOL_SIZE - NUMBER_OF_ADDED_CONNECTIONS) {
            addConnectionToPool();
            LOGGER.info(POOL_HAS_BEEN_EXPANDED_MSG);
        }

        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            if (connection instanceof ProxyConnection) {
                try {
                    if (busy.remove(connection)) {
                        idle.put((ProxyConnection) connection);
                        LOGGER.info(CONNECTION_WAS_RELEASED_MSG);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(CONNECTION_WAS_NOT_RELEASED_MSG);
                }
            }
        }
    }

    @Override
    public void destroy() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        lock.lock();
        try {
            if (initialized.compareAndSet(true, false)) {
                for (ProxyConnection connection : idle) {
                    try {
                        connection.realClose();
                        LOGGER.info(CONNECTION_WAS_CLOSED_MSG);
                    } catch (SQLException e) {
                        LOGGER.error(CONNECTION_WAS_NOT_CLOSED_MSG);
                        throw new CouldNotDestroyConnectionPoolException(FAILED_TO_DESTROY_CONNECTION_POOL_MSG, e);
                    }
                }

                for (ProxyConnection connection : busy) {
                    try {
                        connection.realClose();
                        LOGGER.info(CONNECTION_WAS_CLOSED_MSG);
                    } catch (SQLException e) {
                        LOGGER.error(CONNECTION_WAS_NOT_CLOSED_MSG);
                        throw new CouldNotDestroyConnectionPoolException(FAILED_TO_DESTROY_CONNECTION_POOL_MSG, e);
                    }
                }

                deregisterDrivers();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Registers JDBC drivers
     *
     * @throws CouldNotInitializeConnectionPoolException if failed to initialize connection pool.
     */
    private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            LOGGER.info(DRIVERS_WERE_REGISTERED_MSG);
        } catch (SQLException e) {
            LOGGER.error(DRIVERS_WERE_NOT_REGISTERED_MSG);
            initialized.set(false);
            throw new CouldNotInitializeConnectionPoolException(DRIVER_REGISTRATION_FAILED_MSG, e);
        }
    }

    private void initializePoolByInitSize() throws SQLException {
        for (int i = 0; i < INIT_POOL_SIZE; i++) {
            final Connection connection = DriverManager.getConnection(DATABASE_URL,
                    DATABASE_USERNAME,
                    DATABASE_PASSWORD);
            final ProxyConnection proxyConnection = new ProxyConnection(connection);

            idle.add(proxyConnection);
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
            LOGGER.error(POOL_HAS_NOT_BEEN_EXPANDED_MSG);
        }
    }

    /**
     * Deregisters JDBC drivers
     */
    private void deregisterDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
                LOGGER.info(DRIVERS_WERE_DEREGISTERED_MSG);
            } catch (SQLException e) {
                LOGGER.error(DRIVERS_WERE_NOT_DEREGISTERED_MSG);
            }
        }
    }

}