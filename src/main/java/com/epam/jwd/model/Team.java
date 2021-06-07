package com.epam.jwd.model;

import java.util.Objects;

public class Team extends AbstractBaseEntity {

    private final String country;
    private final Integer rate;

    public Team(Long id, String name, String country, Integer rate) {
        super(id, name);
        this.country = country;
        this.rate = rate;
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
        return Objects.equals(country, team.country) && Objects.equals(rate, team.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country, rate);
    }

    @Override
    public String toString() {
        return "Team{" +
                super.toString() +
                ", country='" + country + '\'' +
                ", rate=" + rate +
                '}';
    }

}