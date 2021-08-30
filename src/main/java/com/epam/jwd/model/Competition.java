package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code Competition} is a sport competition.
 * Extends {@code AbstractBaseEntity} class.
 *
 * @see AbstractBaseEntity
 * @see Team
 */
public class Competition extends AbstractBaseEntity {

    private final Team home;
    private final Team away;

    public Competition(Long id, Team home, Team away) {
        super(id);
        this.home = home;
        this.away = away;
    }

    public Competition(Team home, Team away) {
        this(null, home, away);
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
        return Objects.equals(home, that.home) && Objects.equals(away, that.away);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), home, away);
    }

    @Override
    public String toString() {
        return home.getName() + " – " + away.getName();
    }

}