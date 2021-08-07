package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code Person} is an application person.
 * Extends {@code AbstractBaseEntity} class.
 *
 * @see AbstractBaseEntity
 * @see Role
 */
public class Person extends AbstractBaseEntity {

    private final String login;
    private final String password;
    private final Integer balance;
    private final Role role;

    /**
     * A constructor with ID that used for pull up person from database.
     *
     * @param id       a person ID.
     * @param login    a person login.
     * @param password a person password.
     * @param balance  a person balance.
     * @param role     a person role.
     */
    public Person(Long id, String login, String password, Integer balance, Role role) {
        super(id);
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    /**
     * A constructor without ID that used for save person to database.
     *
     * @param login    a person login.
     * @param password a person password.
     * @param balance  a person balance.
     */
    public Person(String login, String password, Integer balance) {
        this(null, login, password, balance, Role.USER);
    }

    /**
     * @return a person login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return a person password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return a person balance.
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * @return a person role.
     */
    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return Objects.equals(login, person.login) && Objects.equals(password, person.password) && Objects.equals(balance, person.balance) && role == person.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, balance, role);
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Name: " + login +
                ", Balance: " + balance +
                ", Role: " + role.getName();
    }

}