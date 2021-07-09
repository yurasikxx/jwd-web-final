package com.epam.jwd.dao;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;

import java.util.List;

public interface PersonBaseDao extends BaseDao<Person> {

    List<Person> findByRole(Role role) throws DaoException;

}