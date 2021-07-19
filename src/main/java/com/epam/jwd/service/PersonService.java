package com.epam.jwd.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.dao.PersonBaseDao;
import com.epam.jwd.dao.PersonDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PersonService implements PersonBaseService {

    private static final String USER_WAS_NOT_FOUND_BY_GIVEN_ID_MSG = "User wasn't found by given id: %s";
    private static final String USER_WAS_NOT_FOUND_BY_GIVEN_LOGIN_MSG = "User wasn't found by given login: %s";
    private static final String USER_CAN_NOT_LOG_IN_MSG = "User can't log in";

    private static final Logger LOGGER = LogManager.getLogger(PersonService.class);
    private static volatile PersonService instance;
    private final PersonBaseDao personBaseDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;

    private PersonService() {
        this.personBaseDao = PersonDao.getInstance();
        this.hasher = BCrypt.withDefaults();
        this.verifyer = BCrypt.verifyer();
    }

    public static PersonService getInstance() {
        if (instance == null) {
            synchronized (PersonService.class) {
                if (instance == null) {
                    instance = new PersonService();
                }
            }
        }

        return instance;
    }

    @Override
    public void update(Person person) throws DaoException {
        final char[] rawPassword = person.getPassword().toCharArray();
        final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        personBaseDao.update(new Person(person.getId(), person.getLogin(), encryptedPassword, person.getRole()));
    }

    @Override
    public List<Person> findAll() {
        return personBaseDao.findAll();
    }

    @Override
    public Person findById(Long id) throws DaoException, ServiceException {
        return personBaseDao.findById(id).
                orElseThrow(() -> new ServiceException(String.format(USER_WAS_NOT_FOUND_BY_GIVEN_ID_MSG, id)));
    }

    @Override
    public void delete(Long id) throws DaoException {
        personBaseDao.delete(id);
    }

    @Override
    public List<Person> findByRole(Role role) throws DaoException {
        return personBaseDao.findByRole(role);
    }

    @Override
    public Person findByLogin(String login) throws ServiceException, DaoException {
        return personBaseDao.findByLogin(login)
                .orElseThrow(() -> new ServiceException(String.format(USER_WAS_NOT_FOUND_BY_GIVEN_LOGIN_MSG, login)));
    }

    @Override
    public boolean canLogIn(Person person) {
        try {
            final char[] enteredPassword = person.getPassword().toCharArray();
            final Person persistedPerson = this.findByLogin(person.getLogin());
            final char[] encryptedPassword = persistedPerson.getPassword().toCharArray();

            return verifyer.verify(enteredPassword, encryptedPassword).verified;
        } catch (ServiceException | DaoException e) {
            LOGGER.error(USER_CAN_NOT_LOG_IN_MSG);
            return false;
        }
    }

}