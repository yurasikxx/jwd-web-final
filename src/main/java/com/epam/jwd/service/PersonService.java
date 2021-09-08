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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;
import static com.epam.jwd.constant.Constant.INDEX_ROLLBACK_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;

/**
 * A {@code PersonService} class is a person service that is
 * an {@code PersonBaseService} interface implementation.
 *
 * @see BaseService
 * @see PersonBaseService
 */
public class PersonService implements PersonBaseService {

    private static final Logger LOGGER = LogManager.getLogger(PersonService.class);

    private static final String PERSONS_WERE_DESTROYED_MSG = "Persons were destroyed";
    private static final String PERSONS_WERE_NOT_DESTROYED_MSG = "Persons weren't destroyed";
    private static final String PERSON_WAS_NOT_SAVED_MSG = "Person wasn't saved";
    private static final String PERSON_WAS_SAVED_MSG = "Person was saved";
    private static final String PERSON_WAS_UPDATED_MSG = "Person was updated";
    private static final String PERSON_WAS_NOT_UPDATED_MSG = "Person wasn't updated";
    private static final String BALANCE_WAS_UPDATED_MSG = "Balance was updated";
    private static final String BALANCE_WAS_NOT_UPDATED_MSG = "Balance wasn't updated";
    private static final String PERSON_WAS_REGISTERED_MSG = "Person was registered";
    private static final String PERSON_WAS_NOT_REGISTERED_MSG = "Person wasn't registered";
    private static final String PERSONS_WERE_FOUND_MSG = "Persons were found";
    private static final String PERSONS_WERE_NOT_FOUND_MSG = "Persons weren't found";
    private static final String PERSON_WAS_FOUND_MSG = "Person was found";
    private static final String PERSON_WAS_NOT_FOUND_MSG = "Person wasn't found";
    private static final String PERSON_WAS_DELETED_MSG = "Person was deleted";
    private static final String PERSON_WAS_NOT_DELETED_MSG = "Person wasn't deleted";

    private static volatile PersonService instance;

    private final PersonBaseDao personDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;
    private final List<Person> persons;

    private PersonService() {
        this.personDao = PersonDao.getInstance();
        this.hasher = BCrypt.withDefaults();
        this.verifyer = BCrypt.verifyer();
        this.persons = this.findAll();
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
    public void save(Person person) throws ServiceException {
        try {
            personDao.save(person);
            final Person savedPerson = this.findByLogin(person.getLogin());
            this.logIn(savedPerson);

            LOGGER.info(PERSON_WAS_SAVED_MSG);
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_SAVED_MSG);
        }
    }

    @Override
    public void update(Person person) throws ServiceException {
        try {
            personDao.update(person);
            final Person foundPerson = this.findByLogin(person.getLogin());
            persons.remove(foundPerson.getId().intValue() - INDEX_ROLLBACK_VALUE);
            persons.add(foundPerson.getId().intValue() - INDEX_ROLLBACK_VALUE, foundPerson);
            this.logIn(foundPerson);

            LOGGER.info(PERSON_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_UPDATED_MSG);
            throw new ServiceException(PERSON_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public List<Person> findAll() {
        try {
            final List<Person> persons = personDao.findAll();
            LOGGER.info(PERSONS_WERE_FOUND_MSG);

            return persons;
        } catch (DaoException e) {
            LOGGER.error(PERSONS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public Person findById(Long id) {
        Person person = null;

        try {
            final Optional<Person> optionalPerson = personDao.findById(id);

            if (optionalPerson.isPresent()) {
                person = optionalPerson.get();
            }

            LOGGER.info(PERSON_WAS_FOUND_MSG);
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_FOUND_MSG);
        }

        return person;
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            personDao.delete(id);
            LOGGER.info(PERSON_WAS_DELETED_MSG);
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_DELETED_MSG);
            throw new ServiceException(PERSON_WAS_NOT_DELETED_MSG);
        }
    }

    @Override
    public boolean canBeDeleted(Long id) {
        final List<Person> persons = this.findAll();
        final Person person = this.findById(id);

        return persons.contains(person) && id > MIN_INDEX_VALUE;
    }

    @Override
    public void init() throws ServiceException {
        final List<Person> persons = this.findAll();

        for (Person person : persons) {
            this.logIn(person);
        }
    }

    @Override
    public Person register(Person person) throws ServiceException {
        try {
            personDao.save(person);
            final Person savedPerson = this.findByLogin(person.getLogin());
            this.logIn(savedPerson);

            LOGGER.info(PERSON_WAS_REGISTERED_MSG);

            return savedPerson;
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_REGISTERED_MSG);
            throw new ServiceException(PERSON_WAS_NOT_REGISTERED_MSG);
        }
    }

    @Override
    public boolean canRegister(Person person) {
        final List<Person> persons = this.findAll();
        final List<String> logins = new ArrayList<>();

        for (Person iteratedPerson : persons) {
            logins.add(iteratedPerson.getLogin());
        }

        return !logins.contains(person.getLogin());
    }

    @Override
    public void logIn(Person person) throws ServiceException {
        final char[] rawPassword = person.getPassword().toCharArray();
        final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        final Person updatedPerson = new Person(person.getId(), person.getLogin(),
                encryptedPassword, person.getBalance(), person.getRole());

        try {
            personDao.update(updatedPerson);
            LOGGER.info(PERSON_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_UPDATED_MSG);
            throw new ServiceException(PERSON_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public boolean canLogIn(Person person) throws ServiceException {
        final char[] enteredPassword = person.getPassword().toCharArray();
        final Person persistedPerson = this.findByLogin(person.getLogin());
        final char[] encryptedPassword = persistedPerson.getPassword().toCharArray();

        return persistedPerson.getLogin() != null
                && persistedPerson.getPassword() != null
                && verifyer.verify(enteredPassword, encryptedPassword).verified;
    }

    @Override
    public void changePassword(Person person, String password) throws ServiceException {
        final Person updatedPerson = new Person(
                person.getId(), person.getLogin(), password, person.getBalance(), person.getRole());
        this.logIn(updatedPerson);
        persons.remove(updatedPerson.getId().intValue() - INDEX_ROLLBACK_VALUE);
        persons.add(updatedPerson.getId().intValue() - INDEX_ROLLBACK_VALUE, updatedPerson);
    }

    @Override
    public List<Person> findByRole(Role role) {
        try {
            final List<Person> persons = personDao.findByRole(role);
            LOGGER.info(PERSONS_WERE_FOUND_MSG);

            return persons;
        } catch (DaoException e) {
            LOGGER.error(PERSONS_WERE_NOT_FOUND_MSG);
            return Collections.emptyList();
        }
    }

    @Override
    public Person findByLogin(String login) throws ServiceException {
        try {
            final Optional<Person> optionalPerson = personDao.findByLogin(login);

            if (optionalPerson.isPresent()) {
                LOGGER.info(PERSON_WAS_FOUND_MSG);
                return optionalPerson.get();
            }
        } catch (DaoException e) {
            LOGGER.error(PERSON_WAS_NOT_FOUND_MSG);
        }

        throw new ServiceException(PERSON_WAS_NOT_FOUND_MSG);
    }

    @Override
    public void updateBalance(Person person) throws ServiceException {
        try {
            personDao.update(person);
            final Person foundPerson = persons.get((int) (person.getId() - INDEX_ROLLBACK_VALUE));
            final Person placedBetPerson = new Person(person.getId(), person.getLogin(),
                    foundPerson.getPassword(), person.getBalance(), person.getRole());
            persons.remove(foundPerson.getId().intValue() - INDEX_ROLLBACK_VALUE);
            persons.add(foundPerson.getId().intValue() - INDEX_ROLLBACK_VALUE, placedBetPerson);

            LOGGER.info(BALANCE_WAS_UPDATED_MSG);
        } catch (DaoException e) {
            LOGGER.error(BALANCE_WAS_NOT_UPDATED_MSG);
            throw new ServiceException(BALANCE_WAS_NOT_UPDATED_MSG);
        }
    }

    @Override
    public void getNewRegisteredPersons(Person person) {
        persons.add(person);
    }

    @Override
    public void destroy() throws ServiceException {
        for (Person person : persons) {
            try {
                personDao.update(person);
                LOGGER.info(PERSONS_WERE_DESTROYED_MSG);
            } catch (DaoException e) {
                LOGGER.error(PERSONS_WERE_NOT_DESTROYED_MSG);
                throw new ServiceException(PERSONS_WERE_NOT_DESTROYED_MSG);
            }
        }
    }

}