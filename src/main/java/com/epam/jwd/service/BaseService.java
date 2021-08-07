package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;

/**
 * A {@code BaseService} interface is application service
 * that unites entities DAO methods in its methods.
 *
 * @param <T> entity extending {@code BaseEntity} interface.
 */
public interface BaseService<T extends BaseEntity> {

    /**
     * Accepts and corrects entity and save it to database.
     *
     * @param entity a given entity
     * @throws ServiceException if failed to save entity by application causes.
     * @throws DaoException     if failed to save entity by database causes.
     */
    void save(T entity) throws ServiceException, DaoException;

    /**
     * Accepts and corrects entity and update it inside database.
     *
     * @param entity a given entity
     * @throws ServiceException if failed to update entity by application causes.
     * @throws DaoException     if failed to update entity by database causes.
     */
    void update(T entity) throws ServiceException, DaoException;

    /**
     * Looks for entities inside database and returns them.
     *
     * @return a found entities.
     */
    List<T> findAll();

    /**
     * Looks for entity by accepting entity ID and return it.
     *
     * @param id given entity ID.
     * @return a found entity
     * @throws ServiceException if entity wasn't found by application causes.
     * @throws DaoException     if entity wasn't found by database causes.
     */
    T findById(Long id) throws ServiceException, DaoException;

    /**
     * Accepts entity ID and delete it.
     *
     * @param id given entity ID
     * @throws ServiceException if entity wasn't deleted by application causes.
     * @throws DaoException     if entity wasn't deleted by database causes.
     */
    void delete(Long id) throws ServiceException, DaoException;

    /**
     * Indicates if object by given ID can be deleted.
     *
     * @param id given entity ID.
     * @return {@code true} if object can be deleted; {@code false} otherwise.
     * @throws ServiceException if entity cannot be deleted by application causes.
     * @throws DaoException     if entity cannot be deleted by database causes.
     */
    boolean canBeDeleted(Long id) throws ServiceException, DaoException;

}