package com.epam.jwd.model;

import java.util.Objects;

public class Person extends AbstractBaseEntity {

    private final String login;
    private final String password;
    private final Integer balance;
    private final Role role;

    public Person(Long id, String login, String password, Integer balance, Role role) {
        super(id);
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public Person(String login, String password, Integer balance) {
        this(null, login, password, balance, Role.USER);
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getBalance() {
        return balance;
    }

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
        return "Name: " + login +
                ", Role: " + role.getName();
    }

}