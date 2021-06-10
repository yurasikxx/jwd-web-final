package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends BaseEntity> {

    T create(T entity) throws DaoException;

    List<T> findAll() throws DaoException;

    Optional<T> findById(Long id) throws DaoException;

    T update(T entity) throws DaoException;

    void delete(T entity) throws DaoException;

}