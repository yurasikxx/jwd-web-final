package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;

/**
 * A {@code PersonBaseService} interface is an extending of
 * {@code BaseService} interface for person entity.
 *
 * @see BaseService
 * @see PersonService
 */
public interface PersonBaseService extends BaseService<Person> {

    /**
     * Encrypts person password when application is initializing.
     */
    void init();

    /**
     * Registers person by accepting given person data and returns it.
     *
     * @param person a given person
     * @return a registered person
     * @throws DaoException     if person wasn't registered by database causes.
     * @throws ServiceException if person wasn't registered by application causes.
     */
    Person register(Person person) throws DaoException, ServiceException;

    /**
     * Indicates if given person can register.
     *
     * @param person a given person
     * @return {@code true} if person can register; {@code false} otherwise.
     */
    boolean canRegister(Person person);

    /**
     * Authorizes person by accepting given person data.
     *
     * @param person a given person
     * @throws DaoException if person wasn't authorized
     */
    void logIn(Person person) throws DaoException;

    /**
     * Indicates if given person can log in.
     *
     * @param person a given person
     * @return {@code true} if person can log in; {@code false} otherwise.
     */
    boolean canLogIn(Person person);

    /**
     * Looks for persons by accepting given role and return them.
     *
     * @param role a given person role.
     * @return a found persons.
     * @throws DaoException if persons weren't found.
     */
    List<Person> findByRole(Role role) throws DaoException;

    /**
     * Looks for person by accepting person login and return it.
     *
     * @param login a given person login.
     * @return a found person.
     * @throws ServiceException if person wasn't found by application causes.
     * @throws DaoException     if person wasn't found by database causes.
     */
    Person findByLogin(String login) throws ServiceException, DaoException;

    /**
     * Updates person balance by accepting person.
     *
     * @param person a given person.
     * @throws DaoException     if person balance wasn't updated by database causes.
     * @throws ServiceException if person balance wasn't updated by application causes.
     */
    void updateBalance(Person person) throws DaoException, ServiceException;

    /**
     * Puts given person into encrypted person list.
     *
     * @param person a given person.
     */
    void getNewRegisteredPersons(Person person);


    /**
     * Updates person passwords by initial values.
     */
    void destroy();

}