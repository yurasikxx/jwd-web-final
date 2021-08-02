package com.epam.jwd.model;

import java.util.Objects;

public class Betslip extends AbstractBaseEntity {

    private final Competition competition;
    private final BetType betType;
    private final Integer coefficient;

    public Betslip(Long id, Competition competition, BetType betType, Integer coefficient) {
        super(id);
        this.competition = competition;
        this.betType = betType;
        this.coefficient = coefficient;
    }

    public Betslip(Competition competition, BetType betType, Integer coefficient) {
        this(null, competition, betType, coefficient);
    }
    public Betslip(Competition competition, BetType betType) {
        this(null, competition, betType, null);
    }

    public Competition getCompetition() {
        return competition;
    }

    public BetType getBetType() {
        return betType;
    }

    public Integer getCoefficient() {
        return coefficient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Betslip betslip = (Betslip) o;
        return Objects.equals(competition, betslip.competition) && betType == betslip.betType && Objects.equals(coefficient, betslip.coefficient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), competition, betType, coefficient);
    }

    @Override
    public String toString() {
        return competition + ", CF: " + coefficient;
    }

}