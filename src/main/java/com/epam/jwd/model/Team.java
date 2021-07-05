package com.epam.jwd.model;

import java.util.Objects;

public class Team extends AbstractBaseEntity {

    private final String name;
    private final String country;
    private final Integer rate;

    public Team(Long id, String name, String country, Integer rate) {
        super(id);
        this.name = name;
        this.country = country;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Integer getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && Objects.equals(country, team.country) && Objects.equals(rate, team.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, country, rate);
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", rate=" + rate +
                '}';
    }

}