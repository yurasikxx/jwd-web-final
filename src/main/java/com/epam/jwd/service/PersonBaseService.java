package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;

public interface PersonBaseService extends BaseService<Person> {

    void init();

    Person register(Person person) throws DaoException, ServiceException;

    boolean canRegister(Person person);

    void logIn(Person person) throws DaoException;

    boolean canLogIn(Person person);

    List<Person> findByRole(Role role) throws DaoException;

    Person findByLogin(String login) throws ServiceException, DaoException;

    void updateBalance(Person person) throws DaoException, ServiceException;

    void getNewRegisteredPersons(Person person);

    void destroy();

}