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

    /**
     * A constructor with ID user for pull up competition from database.
     *
     * @param id   a competition ID.
     * @param home a home team ID.
     * @param away an away team ID.
     */
    public Competition(Long id, Team home, Team away) {
        super(id);
        this.home = home;
        this.away = away;
    }

    /**
     * A constructor without ID used for save competition to database.
     *
     * @param home a home team ID.
     * @param away an away team ID.
     */
    public Competition(Team home, Team away) {
        this(null, home, away);
    }

    /**
     * @return a competition home team.
     */
    public Team getHome() {
        return home;
    }

    /**
     * @return a competition away team.
     */
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