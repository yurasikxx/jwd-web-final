package com.epam.jwd.dao;

import java.sql.SQLException;

/**
 * A {@code SqlThrowingConsumer} is a functional interface used for find prepared entities.
 *
 * @param <T> any entity
 */
@FunctionalInterface
public interface SqlThrowingConsumer<T> {

    void accept(T t) throws SQLException;

}