package com.epam.jwd.pool;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface {@code ConnectionPool} is an application connection pool.
 * It is used for providing connection to database and releasing it when
 * an application finishes its work.
 *
 * @see ConnectionPoolManager
 * @see ProxyConnection
 */
public interface ConnectionPool {

    /**
     * Initializes the connection pool.
     *
     * @throws CouldNotInitializeConnectionPoolException if failed to initialize connection pool.
     */
    void init() throws CouldNotInitializeConnectionPoolException;

    /**
     * Takes current opened connection and returns it.
     *
     * @return opened connection.
     * @throws SQLException         if there is incorrect SQL query.
     * @throws InterruptedException if there is concurrency problems.
     */
    Connection takeConnection() throws SQLException, InterruptedException;

    /**
     * Releases a connection and put it into idle connections queue.
     *
     * @param connection an opened connection.
     * @throws InterruptedException if there is concurrency problems.
     */
    void releaseConnection(Connection connection) throws InterruptedException;

    /**
     * Destroys connection pool and really closes connections.
     *
     * @throws CouldNotDestroyConnectionPoolException if failed to destroy connection pool.
     * @throws InterruptedException                   if there is concurrency problems.
     */
    void destroy() throws CouldNotDestroyConnectionPoolException, InterruptedException;

}