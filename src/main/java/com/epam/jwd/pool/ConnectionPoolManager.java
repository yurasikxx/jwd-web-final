package com.epam.jwd.pool;

import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolManager implements ConnectionPool {

    private static final int INIT_CONNECTIONS_AMOUNT = 8;
    private static final int MAX_CONNECTIONS_AMOUNT = 32;
    private static final int CONNECTIONS_GROW_FACTOR = 4;

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
                    for (int i = 0; i < INIT_CONNECTIONS_AMOUNT; i++) {
                        final Connection connection = DriverManager
                                .getConnection("jdbc:mysql://localhost:3306/totalizator",
                                        "root", "root");
                        final ProxyConnection proxyConnection = new ProxyConnection(connection);
                        idle.add(proxyConnection);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    initialized.set(false);
                    throw new CouldNotInitializeConnectionPoolException("failed to open connection", e);
                }
                connectionsOpened.set(INIT_CONNECTIONS_AMOUNT);
            }
        } finally {
            init.unlock();
        }
    }

    @Override
    public Connection takeConnection() throws SQLException, InterruptedException {
        ProxyConnection connection = null;

        try {
            connection = idle.take();
            busy.add(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

/*        final int currentOpenedConnections = connectionsOpened.get();
        if (idle.size() <= currentOpenedConnections * 0.25 && currentOpenedConnections < MAX_CONNECTIONS_AMOUNT) {
            // grow connection pool size
        }*/

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
                    e.printStackTrace();
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
                        e.printStackTrace();
                    }
                }

                for (ProxyConnection connection : busy) {
                    try {
                        connection.realClose();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                deregisterDrivers();
            }
        } finally {
            destroy.unlock();
        }
    }

    private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
        System.out.println("sql drivers registration start...");
        try {
            DriverManager.registerDriver(DriverManager.getDriver("jdbc:mysql://localhost:3306/totalizator"));
            System.out.println("registration successful");
        } catch (SQLException e) {
            System.out.println("registration unsuccessful");
            e.printStackTrace();
            initialized.set(false);
            throw new CouldNotInitializeConnectionPoolException("driver registration failed", e);
        }
    }

    private void deregisterDrivers() {
        System.out.println("sql drivers unregistering start...");
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("unregistering drivers failed");
            }
        }
    }

}