package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;

public interface BaseService<T extends BaseEntity> {

    T save (T entity) throws ServiceException, DaoException;

    void update(T entity) throws ServiceException, DaoException;

    List<T> findAll();

    T findById(Long id) throws ServiceException, DaoException;

    void delete(Long id) throws ServiceException, DaoException;

}