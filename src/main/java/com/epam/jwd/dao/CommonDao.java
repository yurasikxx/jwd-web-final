package com.epam.jwd.dao;

import com.epam.jwd.exception.BusinessValidationException;
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
    private static final String FAILED_TO_SAVE_ENTITY_MSG = "Failed to save entity";
    private static final String FAILED_TO_FIND_PREPARED_ENTITIES_MSG = "Failed to find prepared entities";
    private static final String FAILED_TO_UPDATE_ENTITY_MSG = "Failed to update entity";
    private static final String FAILED_TO_DELETE_ENTITY_MSG = "Failed to delete entity";

    private final String selectAllSql;
    private final String findAllSql;
    private final String findByIdSql;

    public CommonDao(String tableName, String selectAllSql, String findAllSql, String findByFieldSql, String idColumnName) {
        this.selectAllSql = String.format(selectAllSql, tableName);
        this.findAllSql = String.format(findAllSql, tableName);
        this.findByIdSql = String.format(findByFieldSql, tableName, idColumnName);
    }

    @Override
    public T save(T entity) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                saveResultSet(resultSet, entity);
            }
        } catch (SQLException | InterruptedException | BusinessValidationException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException(FAILED_TO_SAVE_ENTITY_MSG);
        }

        return entity;
    }

    @Override
    public List<T> findAll() {
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement.setLong(1, id), findByIdSql));
    }

    @Override
    public T update(T entity) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                while (resultSet.next()) {
                    updateResultSet(resultSet, entity);
                }
            }
        } catch (SQLException | InterruptedException | BusinessValidationException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException(FAILED_TO_UPDATE_ENTITY_MSG);
        }

        return entity;
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
            throw new DaoException(FAILED_TO_DELETE_ENTITY_MSG);
        }
    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer, String sql) throws DaoException {
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
            throw new DaoException(FAILED_TO_FIND_PREPARED_ENTITIES_MSG);
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

    protected abstract void saveResultSet(ResultSet resultSet, T entity) throws SQLException, BusinessValidationException;

    protected abstract void updateResultSet(ResultSet resultSet, T entity) throws SQLException, BusinessValidationException;

    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException;

}