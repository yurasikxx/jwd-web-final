package com.epam.jwd.model;

import java.util.Objects;

/**
 * Class {@code BetHistory} is a bet history. Used for logging played bets.
 * Extends {@code AbstractBaseEntity} class.
 *
 * @see AbstractBaseEntity
 * @see Bet
 */
public class BetHistory extends AbstractBaseEntity {

    private final Team home;
    private final Team away;
    private final BetType betType;
    private final Integer coefficient;
    private final Integer betTotal;
    private final String personLogin;
    private final CompetitionResult competitionResult;
    private final BetResult betResult;

    /**
     * A constructor with ID used to pull up bet history from database.
     *
     * @param id                a bet history ID.
     * @param home              a bet history home team.
     * @param away              a bet history away team.
     * @param betType           a bet history bet type.
     * @param coefficient       a bet history coefficient.
     * @param betTotal          a bet history bet total.
     * @param personLogin       a bet history person login.
     * @param competitionResult a bet history competition result.
     * @param betResult         a bet history bet result.
     */
    public BetHistory(Long id, Team home, Team away, BetType betType, Integer coefficient, Integer betTotal,
                      String personLogin, CompetitionResult competitionResult, BetResult betResult) {
        super(id);
        this.home = home;
        this.away = away;
        this.betType = betType;
        this.coefficient = coefficient;
        this.betTotal = betTotal;
        this.personLogin = personLogin;
        this.competitionResult = competitionResult;
        this.betResult = betResult;
    }

    /**
     * A constructor without ID used for save bet history to database.
     *
     * @param home              a bet history home team.
     * @param away              a bet history away team.
     * @param betType           a bet history bet type.
     * @param coefficient       a bet history coefficient.
     * @param betTotal          a bet history bet total.
     * @param personLogin       a bet history person login.
     * @param competitionResult a bet history competition result.
     * @param betResult         a bet history bet result.
     */
    public BetHistory(Team home, Team away, BetType betType, Integer coefficient, Integer betTotal,
                      String personLogin, CompetitionResult competitionResult, BetResult betResult) {
        this(null, home, away, betType, coefficient, betTotal, personLogin, competitionResult, betResult);
    }

    /**
     * @return a bet history home team.
     */
    public Team getHome() {
        return home;
    }

    /**
     * @return a bet history away team.
     */
    public Team getAway() {
        return away;
    }

    /**
     * @return a bet history bet type.
     */
    public BetType getBetType() {
        return betType;
    }

    /**
     * @return a bet history coefficient.
     */
    public Integer getCoefficient() {
        return coefficient;
    }

    /**
     * @return a bet history bet total.
     */
    public Integer getBetTotal() {
        return betTotal;
    }

    /**
     * @return a bet history person login.
     */
    public String getPersonLogin() {
        return personLogin;
    }

    /**
     * @return a bet history competition result.
     */
    public CompetitionResult getCompetitionResult() {
        return competitionResult;
    }

    /**
     * @return a bet history bet result.
     */
    public BetResult getBetResult() {
        return betResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BetHistory that = (BetHistory) o;
        return Objects.equals(home, that.home) && Objects.equals(away, that.away) && betType == that.betType && Objects.equals(coefficient, that.coefficient) && Objects.equals(betTotal, that.betTotal) && Objects.equals(personLogin, that.personLogin) && competitionResult == that.competitionResult && betResult == that.betResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), home, away, betType, coefficient, betTotal, personLogin, competitionResult, betResult);
    }

    @Override
    public String toString() {
        return "BetHistory{" +
                "home=" + home +
                ", away=" + away +
                ", betType=" + betType +
                ", coefficient=" + coefficient +
                ", betTotal=" + betTotal +
                ", personLogin='" + personLogin + '\'' +
                ", competitionResult=" + competitionResult +
                ", betResult=" + betResult +
                '}';
    }

}