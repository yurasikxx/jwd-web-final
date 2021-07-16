package com.epam.jwd.service;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;

public interface PersonBaseService extends BaseService<Person> {

    Person save(Person person) throws DaoException;

    List<Person> findByRole(Role role) throws DaoException;

    Person findByLogin(String login) throws ServiceException, DaoException;

    boolean canLogIn(Person person) throws ServiceException;

}