package com.epam.jwd.service;

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
     *
     * @throws ServiceException if persons weren't initialized.
     */
    void init() throws ServiceException;

    /**
     * Registers person by accepting given person data and returns it.
     *
     * @param person a given person
     * @return a registered person
     * @throws ServiceException if person wasn't registered.
     */
    Person register(Person person) throws ServiceException;

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
     * @throws ServiceException if person wasn't authorized
     */
    void logIn(Person person) throws ServiceException;

    /**
     * Indicates if given person can log in.
     *
     * @param person a given person.
     * @return {@code true} if person can log in; {@code false} otherwise.
     * @throws ServiceException if person wasn't found.
     */
    boolean canLogIn(Person person) throws ServiceException;

    /**
     * Changes person password.
     *
     * @param person   a current person.
     * @param password a new password.
     * @throws ServiceException if password wasn't changed.
     */
    void changePassword(Person person, String password) throws ServiceException;

    /**
     * Looks for persons by accepting given role and return them.
     *
     * @param role a given person role.
     * @return a found persons.
     */
    List<Person> findByRole(Role role);

    /**
     * Looks for person by accepting person login and return it.
     *
     * @param login a given person login.
     * @return a found person.
     * @throws ServiceException if person wasn't found.
     */
    Person findByLogin(String login) throws ServiceException;

    /**
     * Updates person balance by accepting person.
     *
     * @param person a given person.
     * @throws ServiceException if balance wasn't updated.
     */
    void updateBalance(Person person) throws ServiceException;

    /**
     * Puts given person into encrypted person list.
     *
     * @param person a given person.
     */
    void getNewRegisteredPersons(Person person);


    /**
     * Updates person passwords by initial values.
     *
     * @throws ServiceException if persons weren't destroyed.
     */
    void destroy() throws ServiceException;

}