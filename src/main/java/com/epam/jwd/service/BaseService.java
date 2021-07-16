package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;

public interface BaseService<T extends BaseEntity> {

    List<T> findAll();

    T findById(Long id) throws ServiceException, DaoException;

    void delete(Long id) throws DaoException;

}
