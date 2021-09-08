package com.epam.jwd.service;

import com.epam.jwd.exception.CouldNotDestroyConnectionPoolException;
import com.epam.jwd.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Person;
import com.epam.jwd.pool.ConnectionPoolManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class PersonServiceTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final int BALANCE = 1000;

    private final PersonBaseService personService = PersonService.getInstance();

    @BeforeClass
    public static void setUp() throws CouldNotInitializeConnectionPoolException {
        ConnectionPoolManager.getInstance().init();
    }

    @Test
    public void testSaveAndDelete() throws ServiceException {
        personService.save(new Person(LOGIN, PASSWORD, BALANCE));
        final Person person = personService.findByLogin(LOGIN);
        assertNotNull(person);
        assertEquals(LOGIN, person.getLogin());
        personService.delete(person.getId());
    }

    @Test
    public void testFindAll() {
        final List<Person> persons = personService.findAll();
        assertNotNull(persons);
        assertFalse(persons.isEmpty());
    }

    @AfterClass
    public static void tearDown() throws CouldNotDestroyConnectionPoolException, InterruptedException {
        ConnectionPoolManager.getInstance().destroy();
    }

}