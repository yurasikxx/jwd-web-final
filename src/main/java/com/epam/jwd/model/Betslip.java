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
    private final BetType betType;
    private final Integer coefficient;

    /**
     * A constructor with ID used for pull up betslip from database.
     *
     * @param id          a betslip ID.
     * @param competition a betslip competition.
     * @param betType     a betslip bet type.
     * @param coefficient a betslip coefficient.
     */
    public Betslip(Long id, Competition competition, BetType betType, Integer coefficient) {
        super(id);
        this.competition = competition;
        this.betType = betType;
        this.coefficient = coefficient;
    }

    /**
     * A constructor without ID used for save betslip to database.
     *
     * @param competition a betslip competition.
     * @param betType     a betslip bet type.
     * @param coefficient a betslip coefficient.
     */
    public Betslip(Competition competition, BetType betType, Integer coefficient) {
        this(null, competition, betType, coefficient);
    }

    /**
     * A constructor used for save betslip to database with random coefficient.
     *
     * @param competition a betslip competition.
     * @param betType     a betslip bet type.
     */
    public Betslip(Competition competition, BetType betType) {
        this(null, competition, betType, null);
    }

    /**
     * @return a betslip competition.
     */
    public Competition getCompetition() {
        return competition;
    }

    /**
     * @return a betslip bet type.
     */
    public BetType getBetType() {
        return betType;
    }

    /**
     * @return a betslip coefficient.
     */
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