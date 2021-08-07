package com.epam.jwd.dao;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
import com.epam.jwd.model.Role;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.model.Role.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PersonDaoTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String UPDATED_LOGIN = "new";
    private static final int BALANCE = 1000;

    private final PersonBaseDao dao = PersonDao.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSavingAndDeleting() throws DaoException {
        final Person saved = dao.save(new Person(LOGIN, PASSWORD, BALANCE));
        assertNotNull(saved);

        final Optional<Person> optionalPerson = dao.findByLogin(LOGIN);
        assertTrue(optionalPerson.isPresent());

        dao.delete(optionalPerson.get().getId());
    }

    @Test
    public void testUpdating() throws DaoException {
        final Person saved = dao.save(new Person(LOGIN, PASSWORD, BALANCE));
        assertNotNull(saved);

        final Optional<Person> optionalPerson = dao.findByLogin(LOGIN);
        assertTrue(optionalPerson.isPresent());

        final Long id = optionalPerson.get().getId();
        final String password = optionalPerson.get().getPassword();
        final Integer balance = optionalPerson.get().getBalance();
        final Role role = optionalPerson.get().getRole();
        final Person updatable = new Person(id, UPDATED_LOGIN, password, balance, role);
        dao.update(updatable);
        assertNotNull(updatable);
        assertEquals(UPDATED_LOGIN, updatable.getLogin());

        dao.delete(updatable.getId());
    }

    @Test
    public void testFindAll() {
        final List<Person> persons = dao.findAll();
        assertNotNull(persons);
    }

    @Test
    public void testFindById() throws DaoException {
        final Person saved = dao.save(new Person(LOGIN, PASSWORD, BALANCE));
        assertNotNull(saved);

        final Optional<Person> personByLogin = dao.findByLogin(LOGIN);
        assertTrue(personByLogin.isPresent());

        final Optional<Person> personById = dao.findById(personByLogin.get().getId());
        assertTrue(personById.isPresent());
        assertEquals(personByLogin.get().getId(), personById.get().getId());

        dao.delete(personById.get().getId());
    }

    @Test
    public void testFindByRole() throws DaoException {
        final List<Person> personsByRole = dao.findByRole(USER);
        assertNotNull(personsByRole);
        assertFalse(personsByRole.isEmpty());
        assertEquals(USER, personsByRole.get(MIN_INDEX_VALUE).getRole());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}