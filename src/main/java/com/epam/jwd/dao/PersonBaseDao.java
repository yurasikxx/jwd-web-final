package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;
import java.util.Optional;

/**
 * A {@code PersonBaseDao} interface is an extending of
 * {@code BaseDao} interface for person entity.
 *
 * @see BaseDao
 * @see CommonDao
 * @see PersonDao
 */
public interface PersonBaseDao extends BaseDao<Person> {

    /**
     * Looks for person by given login and returns found entity.
     *
     * @param login a given person login
     * @return a found person
     * @throws DaoException if person wasn't found by given ID.
     */
    Optional<Person> findByLogin(String login) throws DaoException;

    /**
     * Looks for persons by given role and returns found entities.
     *
     * @param role a given person role
     * @return a found persons
     * @throws DaoException if persons weren't found by given role.
     */
    List<Person> findByRole(Role role) throws DaoException;

}