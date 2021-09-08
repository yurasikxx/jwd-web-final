package com.epam.jwd.service;

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
     * @throws ServiceException if failed to save entity.
     */
    void save(T entity) throws ServiceException;

    /**
     * Accepts and corrects entity and update it inside database.
     *
     * @param entity a given entity
     * @throws ServiceException if failed to update entity.
     */
    void update(T entity) throws ServiceException;

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
     */
    T findById(Long id);

    /**
     * Accepts entity ID and delete it.
     *
     * @param id given entity ID
     * @throws ServiceException if entity wasn't deleted.
     */
    void delete(Long id) throws ServiceException;

    /**
     * Indicates if object by given ID can be deleted.
     *
     * @param id given entity ID.
     * @return {@code true} if object can be deleted; {@code false} otherwise.
     */
    boolean canBeDeleted(Long id);

}