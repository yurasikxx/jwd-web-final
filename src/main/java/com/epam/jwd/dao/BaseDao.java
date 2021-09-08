package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@code BaseDao} is an abstraction of data access object that is interlayer
 * between application and database management system.
 *
 * @param <T> entity extending {@code BaseEntity} interface.
 * @see CommonDao
 */
public interface BaseDao<T extends BaseEntity> {

    /**
     * Accepts entity and save it to database.
     *
     * @param entity any subject area entity.
     * @return saved entity.
     * @throws DaoException if failed to save entity to database.
     */
    T save(T entity) throws DaoException;

    /**
     * Accepts entity and update it inside database.
     *
     * @param entity an updatable entity
     * @throws DaoException if failed to update entity.
     */
    void update(T entity) throws DaoException;

    /**
     * Looks for all database entities and returns them.
     *
     * @return all subject area entities.
     * @throws DaoException if entities weren't found.
     */
    List<T> findAll() throws DaoException;

    /**
     * Looks for entity by given ID and returns it.
     *
     * @param id an entity ID.
     * @return found by given ID entity.
     * @throws DaoException if failed to find entity.
     */
    Optional<T> findById(Long id) throws DaoException;

    /**
     * Looks for entity by given ID and delete it.
     *
     * @param id an entity ID
     * @throws DaoException if failed to delete entity.
     */
    void delete(Long id) throws DaoException;

}