package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(CommonDao.class);

    private final String selectAllSql;
    private final String findAllSql;
    private final String findByIdSql;

    public CommonDao(String tableName, String selectAllSql, String findAllSql, String findByFieldSql, String idColumnName) {
        this.selectAllSql = String.format(selectAllSql, tableName);
        this.findAllSql = String.format(findAllSql, tableName);
        this.findByIdSql = String.format(findByFieldSql, tableName, idColumnName);
    }

    @Override
    public void save(String... values) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                saveResultSet(resultSet, values);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
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
    public void update(String... values) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                while (resultSet.next()) {
                    updateResultSet(resultSet, values);
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                while (resultSet.next()) {
                    long localId = resultSet.getLong(1);
                    if (localId == id) {
                        resultSet.deleteRow();
                    }
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer, String sql) {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
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
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    protected List<T> findEntities(String sql) {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    protected Optional<T> takeFirstNotNull(List<T> entities) {
        return entities.stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    protected abstract void saveResultSet(ResultSet resultSet, String... values) throws SQLException;

    protected abstract void updateResultSet(ResultSet resultSet, String... values) throws SQLException;

    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException;

}