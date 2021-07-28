package com.epam.jwd.dao;

import com.epam.jwd.exception.BusinessValidationException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PersonDao extends CommonDao<Person> implements PersonBaseDao {

    protected static final int EMPTY_LIST_SIZE_VALUE = 0;
    protected static final int INITIAL_ID_VALUE = 1;
    protected static final int INDEX_ROLLBACK_VALUE = INITIAL_ID_VALUE;
    protected static final int INITIAL_INDEX_VALUE = INITIAL_ID_VALUE;

    private static final String SELECT_ALL_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.p_balance, p.pr_id\n" +
            "from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.p_balance, p.pr_id,\n" +
            "pr.pr_name from %s\n" +
            "join person_role pr on p.pr_id = pr.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.p_balance,\n" +
            "p.pr_id, pr.pr_name from %s\n" +
            "join person_role pr on p.pr_id = pr.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "person p";
    private static final String PERSON_ID_COLUMN = "p.id";
    private static final String PERSON_LOGIN_COLUMN = "p_login";
    private static final String PERSON_PASSWORD_COLUMN = "p_password";
    private static final String PERSON_BALANCE_COLUMN = "p.p_balance";
    private static final String PERSON_ROLE_ID_COLUMN = "pr_id";
    private static final String PERSON_ROLE_NAME_COLUMN = "pr_name";
    private static final String UNAUTHORIZED_UPDATE_MSG = "Unauthorized person can not be updated into database";
    private static final String UNAUTHORIZED_SAVE_MSG = "Unauthorized person can not be saved into database";

    private static volatile PersonDao instance;
    private final String findByLoginSql;
    private final String findByRoleSql;

    public static PersonDao getInstance() {
        if (instance == null) {
            synchronized (PersonDao.class) {
                if (instance == null) {
                    instance = new PersonDao();
                }
            }
        }

        return instance;
    }

    private PersonDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, PERSON_ID_COLUMN);
        this.findByLoginSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, PERSON_LOGIN_COLUMN);
        this.findByRoleSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, PERSON_ROLE_NAME_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Person person) throws BusinessValidationException {
        if (Role.UNAUTHORIZED.equals(person.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_SAVE_MSG);
        }

        try {
            final List<Person> persons = this.findAll();

            if (persons.size() == EMPTY_LIST_SIZE_VALUE) {
                resultSet.moveToInsertRow();
                resultSet.updateLong(PERSON_ID_COLUMN, INITIAL_ID_VALUE);
            } else {
                final AtomicLong personAmount = new AtomicLong(this.findAll().size());
                final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

                if (persons.get(persons.size() - INDEX_ROLLBACK_VALUE).getId().equals(personAmount.get())) {
                    long id = personAmount.incrementAndGet();

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(PERSON_ID_COLUMN, id);
                } else {
                    while (persons.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId().equals(idCounter.get())) {
                        idCounter.incrementAndGet();
                    }

                    resultSet.moveToInsertRow();
                    resultSet.updateLong(PERSON_ID_COLUMN, idCounter.get());
                }
            }

            resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
            resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
            resultSet.updateDouble(PERSON_BALANCE_COLUMN, person.getBalance());
            resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Person person) throws BusinessValidationException {
        if (Role.UNAUTHORIZED.equals(person.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_UPDATE_MSG);
        }

        try {
            long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == person.getId()) {
                resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
                resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
                resultSet.updateDouble(PERSON_BALANCE_COLUMN, person.getBalance());
                resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
                resultSet.updateRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Person mapResultSet(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong(PERSON_ID_COLUMN),
                resultSet.getString(PERSON_LOGIN_COLUMN),
                resultSet.getString(PERSON_PASSWORD_COLUMN),
                resultSet.getDouble(PERSON_BALANCE_COLUMN),
                Role.resolveRoleById(resultSet.getLong(PERSON_ROLE_ID_COLUMN)));
    }

    @Override
    public Optional<Person> findByLogin(String login) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(
                preparedStatement -> preparedStatement.setString(INITIAL_INDEX_VALUE, login), findByLoginSql)
        );
    }

    @Override
    public List<Person> findByRole(Role role) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setString(INITIAL_INDEX_VALUE, role.getName()),
                findByRoleSql);
    }

}