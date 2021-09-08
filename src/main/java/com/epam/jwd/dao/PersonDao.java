package com.epam.jwd.dao;

import com.epam.jwd.exception.BusinessValidationException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.UnknownEnumAttributeException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.epam.jwd.constant.Constant.EMPTY_LIST_SIZE_VALUE;
import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_ID_VALUE;
import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;

/**
 * A {@code PersonDao} class is a person entity data access object that
 * is an implementation of abstract {@code CommonDao} class
 * and {@code PersonBaseDao} interface.
 *
 * @see CommonDao
 * @see PersonBaseDao
 */
public class PersonDao extends CommonDao<Person> implements PersonBaseDao {

    private static final Logger LOGGER = LogManager.getLogger(PersonDao.class);

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
    private static final String UNAUTHORIZED_CHANGING_MSG = "Unauthorized person can not be changed";
    private static final String PERSON_WAS_SAVED_MSG = "Person was saved";
    private static final String PERSON_WAS_NOT_SAVED_MSG = "Person wasn't saved";
    private static final String PERSON_WAS_UPDATED_MSG = "Person was updated";
    private static final String PERSON_WAS_NOT_UPDATED_MSG = "Person wasn't updated";

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
    public Optional<Person> findByLogin(String login) throws DaoException {
        return takeFirstNotNull(findPreparedEntities(preparedStatement -> preparedStatement
                .setString(INITIAL_INDEX_VALUE, login), findByLoginSql));
    }

    @Override
    public List<Person> findByRole(Role role) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement
                .setString(INITIAL_INDEX_VALUE, role.getName()), findByRoleSql);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, Person person) throws BusinessValidationException {
        personRoleValidate(person);

        try {
            final List<Person> persons = this.findAll();
            final AtomicLong personAmount = new AtomicLong(persons.size());
            final AtomicLong idCounter = new AtomicLong(INITIAL_ID_VALUE);

            setId(resultSet, persons, personAmount, idCounter);

            resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
            resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
            resultSet.updateInt(PERSON_BALANCE_COLUMN, person.getBalance());
            resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
            resultSet.insertRow();
            resultSet.moveToCurrentRow();

            LOGGER.info(PERSON_WAS_SAVED_MSG);
        } catch (SQLException | DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, Person person) throws BusinessValidationException, DaoException {
        personRoleValidate(person);

        try {
            final long id = resultSet.getLong(INITIAL_INDEX_VALUE);

            if (id == person.getId()) {
                resultSet.updateString(PERSON_LOGIN_COLUMN, person.getLogin());
                resultSet.updateString(PERSON_PASSWORD_COLUMN, person.getPassword());
                resultSet.updateInt(PERSON_BALANCE_COLUMN, person.getBalance());
                resultSet.updateLong(PERSON_ROLE_ID_COLUMN, person.getRole().getId());
                resultSet.updateRow();

                LOGGER.info(PERSON_WAS_UPDATED_MSG);
            }
        } catch (SQLException e) {
            LOGGER.error(PERSON_WAS_NOT_UPDATED_MSG);
            throw new DaoException(PERSON_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    protected Person mapResultSet(ResultSet resultSet) throws SQLException, UnknownEnumAttributeException {
        return new Person(resultSet.getLong(PERSON_ID_COLUMN),
                resultSet.getString(PERSON_LOGIN_COLUMN),
                resultSet.getString(PERSON_PASSWORD_COLUMN),
                resultSet.getInt(PERSON_BALANCE_COLUMN),
                Role.resolveRoleById(resultSet.getLong(PERSON_ROLE_ID_COLUMN)));
    }

    private void personRoleValidate(Person person) throws BusinessValidationException {
        if (Role.UNAUTHORIZED.equals(person.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_CHANGING_MSG);
        }
    }

    private void setId(ResultSet resultSet, List<Person> persons, AtomicLong personAmount, AtomicLong idCounter)
            throws SQLException {
        if (persons.size() == EMPTY_LIST_SIZE_VALUE) {
            setFirstId(resultSet);
        } else {
            setCustomId(resultSet, persons, personAmount, idCounter);
        }
    }

    private void setFirstId(ResultSet resultSet) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateLong(PERSON_ID_COLUMN, INITIAL_ID_VALUE);
    }

    private void setCustomId(ResultSet resultSet, List<Person> persons, AtomicLong personAmount, AtomicLong idCounter)
            throws SQLException {
        final Long lastPersonId = persons.get(persons.size() - INDEX_ROLLBACK_VALUE).getId();

        if (lastPersonId.equals(personAmount.get())) {
            final long id = personAmount.incrementAndGet();

            resultSet.moveToInsertRow();
            resultSet.updateLong(PERSON_ID_COLUMN, id);
        } else {
            while (getIntermediatePersonId(persons, idCounter).equals(idCounter.get())) {
                idCounter.incrementAndGet();
            }

            resultSet.moveToInsertRow();
            resultSet.updateLong(PERSON_ID_COLUMN, idCounter.get());
        }
    }

    private Long getIntermediatePersonId(List<Person> persons, AtomicLong idCounter) {
        return persons.get((int) (idCounter.get() - INDEX_ROLLBACK_VALUE)).getId();
    }

}