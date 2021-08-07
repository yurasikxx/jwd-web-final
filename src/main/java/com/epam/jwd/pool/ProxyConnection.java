package com.epam.jwd.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * A {@code ProxyConnection} class delegates @{@code Connection} class methods
 * and puts opened connections into connection pool instead of closing them.
 *
 * @see ConnectionPool
 * @see ConnectionPoolManager
 */
public class ProxyConnection implements Connection {

    private final Connection actualConnection;

    public ProxyConnection(Connection actualConnection) {
        this.actualConnection = actualConnection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return actualConnection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return actualConnection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return actualConnection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return actualConnection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        actualConnection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return actualConnection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        actualConnection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        actualConnection.rollback();
    }

    /**
     * Puts connection to pool instead of its real closing.
     */
    @Override
    public void close() {
        ConnectionPoolManager.getInstance().releaseConnection(this);
    }

    /**
     * Really closes connection.
     */
    void realClose() throws SQLException {
        actualConnection.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return actualConnection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return actualConnection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        actualConnection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return actualConnection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        actualConnection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return actualConnection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        actualConnection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return actualConnection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return actualConnection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        actualConnection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return actualConnection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return actualConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return actualConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return actualConnection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        actualConnection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        actualConnection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return actualConnection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return actualConnection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return actualConnection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        actualConnection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        actualConnection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return actualConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return actualConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return actualConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return actualConnection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return actualConnection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return actualConnection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return actualConnection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return actualConnection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return actualConnection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return actualConnection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return actualConnection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        actualConnection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        actualConnection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return actualConnection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return actualConnection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return actualConnection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return actualConnection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        actualConnection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return actualConnection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        actualConnection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        actualConnection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return actualConnection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return actualConnection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return actualConnection.isWrapperFor(iface);
    }

}