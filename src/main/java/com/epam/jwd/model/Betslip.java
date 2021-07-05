package com.epam.jwd.model;

import java.util.Objects;

public class Betslip extends AbstractBaseEntity {

    private final Double coefficient;
    private final Competition competition;
    private final BetType betType;

    public Betslip(Long id, Double coefficient, Competition competition, BetType betType) {
        super(id);
        this.coefficient = coefficient;
        this.competition = competition;
        this.betType = betType;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public Competition getCompetition() {
        return competition;
    }

    public BetType getBetType() {
        return betType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Betslip betslip = (Betslip) o;
        return Objects.equals(coefficient, betslip.coefficient) && Objects.equals(competition, betslip.competition) && betType == betslip.betType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coefficient, competition, betType);
    }

    @Override
    public String toString() {
        return "Betslip{" +
                "coefficient=" + coefficient +
                ", competition=" + competition +
                ", betType=" + betType +
                '}';
    }

}