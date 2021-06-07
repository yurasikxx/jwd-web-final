package com.epam.jwd.model;

import java.util.Objects;

public class Bet extends AbstractBaseEntity {

    private final Competition competition;
    private final BetType betType;
    private final Person person;

    public Bet(Long id, String name, Competition competition, BetType betType, Person person) {
        super(id, name);
        this.competition = competition;
        this.betType = betType;
        this.person = person;
    }

    public Competition getCompetition() {
        return competition;
    }

    public BetType getBetType() {
        return betType;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return Objects.equals(competition, bet.competition) && betType == bet.betType && Objects.equals(person, bet.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(competition, betType, person);
    }

    @Override
    public String toString() {
        return "Bet{" +
                super.toString() +
                ", competition=" + competition +
                ", betType=" + betType +
                ", person=" + person +
                '}';
    }

}