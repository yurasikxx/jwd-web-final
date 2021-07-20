package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends BaseEntity> {

    T save(T entity) throws DaoException;

    List<T> findAll();

    Optional<T> findById(Long id) throws DaoException;

    void update(T entity) throws DaoException;

    void delete(Long id) throws DaoException;

}