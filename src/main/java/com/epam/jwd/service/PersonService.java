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

import java.util.ArrayList;
import java.util.List;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;

public class PersonService implements PersonBaseService {

    private static final Logger LOGGER = LogManager.getLogger(PersonService.class);
    private static final String USER_WAS_NOT_FOUND_BY_GIVEN_ID_MSG = "User wasn't found by given id: %s";
    private static final String USER_WAS_NOT_FOUND_BY_GIVEN_LOGIN_MSG = "User wasn't found by given login: %s";
    private static final String USER_CAN_NOT_LOG_IN_MSG = "User can't log in";

    private static volatile PersonService instance;
    private final PersonBaseDao personDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;
    private final List<Person> persons;
    private final List<Person> newRegisteredPersons;

    private PersonService() {
        this.personDao = PersonDao.getInstance();
        this.hasher = BCrypt.withDefaults();
        this.verifyer = BCrypt.verifyer();
        this.persons = this.findAll();
        this.newRegisteredPersons = new ArrayList<>();
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
    public void init() {
        for (int i = 0; i < this.findAll().size(); i++) {
            try {
                this.update(this.findAll().get(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Person save(Person person) throws DaoException {
        personDao.save(person);
        final Person savedPerson = this.findAll().get(this.findAll().size() - 1);
        this.update(savedPerson);
        return savedPerson;
    }

    @Override
    public boolean canRegister(Person person) {
        final List<Person> persons = personDao.findAll();
        final List<String> logins = new ArrayList<>();

        for (Person iteratedPerson : persons) {
            logins.add(iteratedPerson.getLogin());
        }

        return !logins.contains(person.getLogin()) && person.getLogin().length() <= 40 && person.getPassword().length() <= 100;
    }

    @Override
    public void update(Person person) throws DaoException {
        final char[] rawPassword = person.getPassword().toCharArray();
        final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        personDao.update(new Person(person.getId(), person.getLogin(), encryptedPassword, person.getRole()));
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

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public Person findById(Long id) throws DaoException, ServiceException {
        return personDao.findById(id).
                orElseThrow(() -> new ServiceException(String.format(USER_WAS_NOT_FOUND_BY_GIVEN_ID_MSG, id)));
    }

    @Override
    public void delete(Long id) throws DaoException {
        personDao.delete(id);
    }

    @Override
    public List<Person> findByRole(Role role) throws DaoException {
        return personDao.findByRole(role);
    }

    @Override
    public Person findByLogin(String login) throws ServiceException, DaoException {
        return personDao.findByLogin(login)
                .orElseThrow(() -> new ServiceException(String.format(USER_WAS_NOT_FOUND_BY_GIVEN_LOGIN_MSG, login)));
    }

    @Override
    public void getNewRegisteredPersons(Person person) {
        newRegisteredPersons.add(person);
    }

    @Override
    public void destroy() {
        persons.addAll(newRegisteredPersons);

        for (Person person : persons) {
            try {
                personDao.update(person);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

}