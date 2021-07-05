package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.BaseEntity;
import com.epam.jwd.pool.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CommonDao<T extends BaseEntity> implements BaseDao<T> {

    private static final String FIND_ALL_SQL_QUERY = "select b.id, b.bet_total, bs.id, bs.coefficient, c.id, s.s_name,\n" +
            " t_home.id, t_home.t_name, t_home.t_country, t_home.t_rate, \n" +
            " t_away.id, t_away.t_name, t_away.t_country, t_away.t_rate,\n" +
            " bt_name, p.id, p.p_login, p.p_password, pr.pr_name from bet b\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team t_home on c.t_home_id = t_home.id\n" +
            "join team t_away on c.t_away_id = t_away.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id;";
    private static final String FIND_BY_ID_SQL_QUERY = "select b.id, b.bet_total, bs.id, bs.coefficient, c.id, s.s_name,\n" +
            " t_home.id, t_home.t_name, t_home.t_country, t_home.t_rate, \n" +
            " t_away.id, t_away.t_name, t_away.t_country, t_away.t_rate,\n" +
            " bt_name, p.id, p.p_login, p.p_password, pr.pr_name from bet b\n" +
            "join betslip bs on b.bs_id = bs.id\n" +
            "join competition c on bs.c_id = c.id\n" +
            "join sport s on c.s_id = s.id\n" +
            "join team t_home on c.t_home_id = t_home.id\n" +
            "join team t_away on c.t_away_id = t_away.id\n" +
            "join bet_type bt on bs.bt_id = bt.id\n" +
            "join person p on b.p_id = p.id\n" +
            "join person_role pr on p.pr_id = pr.id\n" +
            "where b.id = ?;";

    private final String tableName;
    private final String findAllSql;
    private final String findByIdSql;

    public CommonDao(String tableName) {
        this.tableName = tableName;
        this.findAllSql = String.format(FIND_ALL_SQL_QUERY, tableName);
        this.findByIdSql = String.format(FIND_BY_ID_SQL_QUERY, tableName);
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public T create(T entity) throws DaoException {
        return null;
    }

    @Override
    public List<T> findAll() throws DaoException {
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement.setLong(1, id), findByIdSql));
    }

    @Override
    public T update(T entity) throws DaoException {
        return null;
    }

    @Override
    public void delete(T entity) throws DaoException {

    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer,
                                           String sql) {
        try (final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            preparationConsumer.accept(statement);
            try (final ResultSet resultSet = statement.executeQuery()) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println("user name read unsuccessfully");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    protected List<T> findEntities(String sql) {
        try (final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
             final Statement statement = conn.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println("user name read unsuccessfully");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    protected Optional<T> takeFirstNotNull(List<T> entities) {
        return entities.stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException;

}