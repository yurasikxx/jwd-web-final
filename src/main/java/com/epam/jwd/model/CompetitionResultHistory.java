package com.epam.jwd.model;

import java.util.Objects;

public class CompetitionResultHistory extends AbstractBaseEntity {

    private final Competition competition;
    private final Team team;

    public CompetitionResultHistory(Long id, Competition competition, Team team) {
        super(id);
        this.competition = competition;
        this.team = team;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompetitionResultHistory that = (CompetitionResultHistory) o;
        return Objects.equals(competition, that.competition) && Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), competition, team);
    }

    @Override
    public String toString() {
        return "CompetitionResultHistory{" +
                "competition=" + competition +
                ", team=" + team +
                '}';
    }

}