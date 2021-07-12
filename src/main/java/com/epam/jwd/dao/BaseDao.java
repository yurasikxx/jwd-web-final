package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends BaseEntity> {

    void save(String... values) throws DaoException;

    List<T> findAll() throws DaoException;

    Optional<T> findById(Long id) throws DaoException;

    void update(String... values) throws DaoException;

    void delete(Long id) throws DaoException;

}