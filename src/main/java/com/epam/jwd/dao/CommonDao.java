package com.epam.jwd.dao;

import com.epam.jwd.exception.BusinessValidationException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.ENTITIES_WERE_FOUND_MSG;
import static com.epam.jwd.constant.Constant.ENTITIES_WERE_NOT_FOUND_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_DELETED_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_NOT_DELETED_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_NOT_SAVED_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_NOT_UPDATED_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_SAVED_MSG;
import static com.epam.jwd.constant.Constant.ENTITY_WAS_UPDATED_MSG;
import static com.epam.jwd.constant.Constant.FAILED_TO_DELETE_ENTITY_MSG;
import static com.epam.jwd.constant.Constant.FAILED_TO_FIND_PREPARED_ENTITIES_MSG;
import static com.epam.jwd.constant.Constant.FAILED_TO_SAVE_ENTITY_MSG;
import static com.epam.jwd.constant.Constant.FAILED_TO_UPDATE_ENTITY_MSG;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.PREPARED_ENTITIES_WERE_FOUND;
import static com.epam.jwd.constant.Constant.PREPARED_ENTITIES_WERE_NOT_FOUND_MSG;

/**
 * Abstract {@code CommonDao} class is an interlayer
 * between {@code BaseDao} and other entity DAOs.
 *
 * @param <T> entity extending {@code BaseEntity} interface.
 * @see BaseDao
 */
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
    public T save(T entity) throws DaoException {
        return saveEntities(entity);
    }

    @Override
    public void update(T entity) throws DaoException {
        updateEntities(entity);
    }

    @Override
    public List<T> findAll() throws DaoException {
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement
                .setLong(INITIAL_INDEX_VALUE, id), findByIdSql));
    }

    @Override
    public void delete(Long id) throws DaoException {
        deleteEntities(id);
    }

    private T saveEntities(T entity) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection()) {
            connection.setAutoCommit(false);

            try (final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                saveResultSet(entity, statement);
                connection.commit();
                LOGGER.info(ENTITY_WAS_SAVED_MSG);
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.error(ENTITY_WAS_NOT_SAVED_MSG);
                throw new DaoException(ENTITY_WAS_NOT_SAVED_MSG);
            } finally {
                connection.setAutoCommit(true);
            }

            return entity;
        } catch (SQLException | InterruptedException | BusinessValidationException e) {
            LOGGER.error(FAILED_TO_SAVE_ENTITY_MSG);
            throw new DaoException(FAILED_TO_SAVE_ENTITY_MSG);
        }
    }

    private void saveResultSet(T entity, Statement statement) throws SQLException, BusinessValidationException {
        try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
            saveResultSet(resultSet, entity);
        }
    }

    private void updateEntities(T entity) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            connection.setAutoCommit(false);

            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                while (resultSet.next()) {
                    updateResultSet(resultSet, entity);
                }

                connection.commit();
                LOGGER.info(ENTITY_WAS_UPDATED_MSG);
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.error(ENTITY_WAS_NOT_UPDATED_MSG);
                throw new DaoException(ENTITY_WAS_NOT_UPDATED_MSG);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException | InterruptedException | BusinessValidationException e) {
            LOGGER.error(FAILED_TO_UPDATE_ENTITY_MSG);
            throw new DaoException(FAILED_TO_UPDATE_ENTITY_MSG);
        }
    }

    private void deleteEntities(Long id) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            connection.setAutoCommit(false);

            try (final ResultSet resultSet = statement.executeQuery(selectAllSql)) {
                while (resultSet.next()) {
                    deleteResultSet(id, resultSet);
                }

                connection.commit();
                LOGGER.info(ENTITY_WAS_DELETED_MSG);
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                LOGGER.error(ENTITY_WAS_NOT_DELETED_MSG);
                throw new DaoException(ENTITY_WAS_NOT_DELETED_MSG);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | InterruptedException e) {
            LOGGER.error(FAILED_TO_DELETE_ENTITY_MSG);
            throw new DaoException(FAILED_TO_DELETE_ENTITY_MSG);
        }
    }

    private void deleteResultSet(Long id, ResultSet resultSet) throws SQLException {
        final long localId = resultSet.getLong(INITIAL_INDEX_VALUE);

        if (localId == id) {
            resultSet.deleteRow();
        }
    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer, String sql)
            throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            preparationConsumer.accept(statement);

            try (final ResultSet resultSet = statement.executeQuery()) {
                final List<T> entities = new ArrayList<>();

                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }

                LOGGER.info(PREPARED_ENTITIES_WERE_FOUND);
                return entities;
            }
        } catch (SQLException | InterruptedException | UnknownEnumAttributeException e) {
            LOGGER.error(PREPARED_ENTITIES_WERE_NOT_FOUND_MSG);
            throw new DaoException(FAILED_TO_FIND_PREPARED_ENTITIES_MSG);
        }
    }

    protected List<T> findEntities(String sql) throws DaoException {
        try (final Connection connection = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = connection.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                final List<T> entities = new ArrayList<>();

                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }

                LOGGER.info(ENTITIES_WERE_FOUND_MSG);

                return entities;
            }
        } catch (SQLException | InterruptedException | UnknownEnumAttributeException e) {
            LOGGER.error(ENTITIES_WERE_NOT_FOUND_MSG);
            throw new DaoException(ENTITIES_WERE_NOT_FOUND_MSG);
        }
    }

    protected Optional<T> takeFirstNotNull(List<T> entities) {
        return entities.stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Saves entity to database.
     *
     * @param resultSet a given result set.
     * @param entity    a given entity.
     * @throws BusinessValidationException if unauthorized person will try save entity.
     */
    protected abstract void saveResultSet(ResultSet resultSet, T entity) throws BusinessValidationException;

    /**
     * Updates entity inside database.
     *
     * @param resultSet a given result set.
     * @param entity    a given entity.
     * @throws BusinessValidationException if unauthorized person will try update entity.
     * @throws DaoException                if entity wasn't updated.
     */
    protected abstract void updateResultSet(ResultSet resultSet, T entity)
            throws BusinessValidationException, DaoException;

    /**
     * Retrieves objects from the database.
     *
     * @param resultSet a given result set.
     * @return a retrieved entity.
     * @throws SQLException                  if failed to execute query.
     * @throws UnknownEnumAttributeException if there is unknown enum constant.
     */
    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException;

}