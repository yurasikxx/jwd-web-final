package com.epam.jwd.model;

import java.util.Objects;

public class Person extends AbstractBaseEntity {

    private final String login;
    private final String password;
    private final Role role;

    public Person(Long id, String name, String login, String password, Role role) {
        super(id, name);
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(login, person.login) && Objects.equals(password, person.password) && role == person.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, role);
    }

    @Override
    public String toString() {
        return "Person{" +
                super.toString() +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

}