package com.epam.jwd.pool;

import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

    void init() throws CouldNotInitializeConnectionPoolException;

    Connection takeConnection() throws SQLException, InterruptedException;

    void releaseConnection(Connection connection) throws InterruptedException;

    void destroy() throws InterruptedException;

}
