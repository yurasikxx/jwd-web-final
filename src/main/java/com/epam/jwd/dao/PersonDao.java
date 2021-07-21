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

    private static final String SELECT_ALL_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.pr_id from %s;";
    private static final String FIND_ALL_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.pr_id, pr.pr_name\n" +
            "from %s\n" +
            "join person_role pr on p.pr_id = pr.id;";
    private static final String FIND_BY_FIELD_SQL_QUERY = "select p.id, p.p_login, p.p_password, p.pr_id, pr.pr_name\n" +
            "from %s\n" +
            "join person_role pr on p.pr_id = pr.id\n" +
            "where %s = ?;";
    private static final String TABLE_NAME = "person p";
    private static final String PERSON_ID_COLUMN = "p.id";
    private static final String PERSON_LOGIN_COLUMN = "p_login";
    private static final String PERSON_PASSWORD_COLUMN = "p_password";
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
    protected void saveResultSet(ResultSet resultSet, Person person) throws SQLException, BusinessValidationException {
        if (Role.UNAUTHORIZED.equals(person.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_SAVE_MSG);
        }

        final AtomicLong personAmount = new AtomicLong(this.findAll().size());
        final AtomicLong idCounter = new AtomicLong(1);
        final List<Person> persons = this.findAll();

        if (personAmount.get() == persons.get(persons.size() - 1).getId()) {
            long id = personAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(PERSON_ID_COLUMN, id);
        } else {
            while (idCounter.get() == persons.get((int) (idCounter.get() - 1)).getId()) {
                idCounter.incrementAndGet();
            }

            resultSet.moveToInsertRow();
            resultSet.updateLong(PERSON_ID_COLUMN, idCounter.get());
        }

        resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
        resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
        resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
        resultSet.insertRow();
        resultSet.moveToCurrentRow();

    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Person person) throws SQLException, BusinessValidationException {
        if (Role.UNAUTHORIZED.equals(person.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_UPDATE_MSG);
        }

        long id = resultSet.getLong(1);

        if (id == person.getId()) {
            resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
            resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
            resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
            resultSet.updateRow();
        }
    }

    @Override
    protected Person mapResultSet(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong(PERSON_ID_COLUMN),
                resultSet.getString(PERSON_LOGIN_COLUMN),
                resultSet.getString(PERSON_PASSWORD_COLUMN),
                Role.resolveRoleById(resultSet.getLong(PERSON_ROLE_ID_COLUMN)));
    }

    @Override
    public Optional<Person> findByLogin(String login) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(
                preparedStatement -> preparedStatement.setString(1, login), findByLoginSql)
        );
    }

    @Override
    public List<Person> findByRole(Role role) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setString(1, role.getName()),
                findByRoleSql);
    }

}