package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code Betslip} is a sport competition betslip.
 * Extends {@code AbstractBaseEntity} class.
 *
 * @see AbstractBaseEntity
 * @see Competition
 */
public class Betslip extends AbstractBaseEntity {

    private final Competition competition;
    private final BetslipType betslipType;
    private final Integer coefficient;

    public Betslip(Long id, Competition competition, BetslipType betslipType, Integer coefficient) {
        super(id);
        this.competition = competition;
        this.betslipType = betslipType;
        this.coefficient = coefficient;
    }

    public Betslip(Competition competition, BetslipType betslipType, Integer coefficient) {
        this(null, competition, betslipType, coefficient);
    }

    public Betslip(Competition competition, BetslipType betslipType) {
        this(null, competition, betslipType, null);
    }

    public Competition getCompetition() {
        return competition;
    }

    public BetslipType getBetslipType() {
        return betslipType;
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
        return Objects.equals(competition, betslip.competition) && betslipType == betslip.betslipType && Objects.equals(coefficient, betslip.coefficient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), competition, betslipType, coefficient);
    }

    @Override
    public String toString() {
        return competition + ", CF: " + coefficient;
    }

}