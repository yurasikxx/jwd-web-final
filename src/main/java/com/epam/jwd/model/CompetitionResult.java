package com.epam.jwd.model;

public enum CompetitionResult implements BaseEntity {

    HOME_WIN(1L, "Home win"),
    DRAW(2L, "Draw"),
    AWAY_TEAM(3L, "Away win");

    private final Long id;
    private final String name;

    CompetitionResult(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CompetitionResult resolveCompetitionResultById(Long id) {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getId().equals(id)) {
                return competitionResult;
            }
        }

        throw new IllegalArgumentException("Unknown competition result ID");
    }

    public static CompetitionResult resolveCompetitionResultByName(String name) {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getName().equals(name)) {
                return competitionResult;
            }
        }

        throw new IllegalArgumentException("Unknown competition result name");
    }

}