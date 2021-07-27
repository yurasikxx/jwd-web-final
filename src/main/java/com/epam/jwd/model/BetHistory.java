package com.epam.jwd.model;

import java.util.Objects;

public class BetHistory extends AbstractBaseEntity {

    private final Team home;
    private final Team away;
    private final BetType betType;
    private final Double coefficient;
    private final Integer betTotal;
    private final String personLogin;
    private final CompetitionResult competitionResult;
    private final BetResult betResult;

    public BetHistory(Long id, Team home, Team away, BetType betType, Double coefficient, Integer betTotal,
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


    public Team getHome() {
        return home;
    }

    public Team getAway() {
        return away;
    }

    public BetType getBetType() {
        return betType;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public Integer getBetTotal() {
        return betTotal;
    }

    public String getPersonLogin() {
        return personLogin;
    }

    public CompetitionResult getCompetitionResult() {
        return competitionResult;
    }

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