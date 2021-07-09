package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CommonDao<T extends BaseEntity> implements BaseDao<T> {

    private final String findAllSql;
    private final String findByIdSql;

    public CommonDao(String tableName, String findAllSql, String findByFieldSql, String idColumnName) {
        this.findAllSql = String.format(findAllSql, tableName);
        this.findByIdSql = String.format(findByFieldSql, tableName, idColumnName);
    }

    @Override
    public T create(T entity) throws DaoException {
        return null;
    }

    @Override
    public List<T> findAll() throws DaoException {
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement.setLong(1, id), findByIdSql));
    }

    @Override
    public T update(T entity) throws DaoException {
        return null;
    }

    @Override
    public void delete(T entity) throws DaoException {

    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer,
                                           String sql) {
        try (final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            preparationConsumer.accept(statement);
            try (final ResultSet resultSet = statement.executeQuery()) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    protected List<T> findEntities(String sql) {
        try (final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = conn.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    protected Optional<T> takeFirstNotNull(List<T> entities) {
        return entities.stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException;

}