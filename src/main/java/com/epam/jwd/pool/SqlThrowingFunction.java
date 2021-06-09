package com.epam.jwd.pool;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlThrowingFunction<T, R> {

    R apply(T t) throws SQLException;

}