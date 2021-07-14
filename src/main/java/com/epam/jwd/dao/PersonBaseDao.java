package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;
import java.util.Optional;

public interface PersonBaseDao extends BaseDao<Person> {

    Optional<Person> findByLogin(String login) throws DaoException;

    List<Person> findByRole(Role role) throws DaoException;

}