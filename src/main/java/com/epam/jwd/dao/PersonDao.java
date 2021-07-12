package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    private final String findByRoleSql;

    public PersonDao() {
        super(TABLE_NAME, SELECT_ALL_SQL_QUERY, FIND_ALL_SQL_QUERY, FIND_BY_FIELD_SQL_QUERY, PERSON_ID_COLUMN);
        this.findByRoleSql = String.format(FIND_BY_FIELD_SQL_QUERY, TABLE_NAME, PERSON_ROLE_NAME_COLUMN);
    }

    @Override
    protected void saveResultSet(ResultSet resultSet, String... values) throws SQLException {
        resultSet.moveToInsertRow();
        resultSet.updateInt(PERSON_ID_COLUMN, Integer.parseInt(values[0]));
        resultSet.updateString(PERSON_LOGIN_COLUMN, values[1]);
        resultSet.updateString(PERSON_PASSWORD_COLUMN, values[2]);
        resultSet.updateInt(PERSON_ROLE_ID_COLUMN, Integer.parseInt(values[3]));
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
    }

    @Override
    protected void updateResultSet(ResultSet resultSet, String... values) throws SQLException {
        long id = resultSet.getLong(1);

        if (id == Long.parseLong(values[0])) {
            resultSet.updateString(PERSON_LOGIN_COLUMN, values[1]);
            resultSet.updateString(PERSON_PASSWORD_COLUMN, values[2]);
            resultSet.updateInt(PERSON_ROLE_ID_COLUMN, Integer.parseInt(values[3]));
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
    public List<Person> findByRole(Role role) throws DaoException {
        return findPreparedEntities(preparedStatement -> preparedStatement.setString(1, role.getName()),
                findByRoleSql);
    }

}