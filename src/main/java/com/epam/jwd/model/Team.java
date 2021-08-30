package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code Team} is a sport team.
 *
 * @see AbstractBaseEntity
 * @see Sport
 */
public class Team extends AbstractBaseEntity {

    private final String name;
    private final Sport sport;

    public Team(Long id, String name, Sport sport) {
        super(id);
        this.name = name;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && sport == team.sport;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, sport);
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", sport=" + sport +
                '}';
    }

}