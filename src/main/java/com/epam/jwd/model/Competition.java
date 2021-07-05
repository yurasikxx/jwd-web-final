package com.epam.jwd.model;

import java.util.Objects;

public class Competition extends AbstractBaseEntity {

    private final Sport sport;
    private final Team home;
    private final Team away;

    public Competition(Long id, Sport sport, Team home, Team away) {
        super(id);
        this.sport = sport;
        this.home = home;
        this.away = away;
    }

    public Sport getSport() {
        return sport;
    }

    public Team getHome() {
        return home;
    }

    public Team getAway() {
        return away;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Competition that = (Competition) o;
        return sport == that.sport && Objects.equals(home, that.home) && Objects.equals(away, that.away);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sport, home, away);
    }

    @Override
    public String toString() {
        return "Competition{" +
                "sport=" + sport +
                ", home=" + home +
                ", away=" + away +
                '}';
    }

}