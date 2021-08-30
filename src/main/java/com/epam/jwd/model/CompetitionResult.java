package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

/**
 * Enum {@code CompetitionResult} is a competition result. Implements {@code BaseEntity} interface.
 *
 * @see Competition
 */
public enum CompetitionResult implements BaseEntity {

    HOME_WIN(1L, "Home win"),
    DRAW(2L, "Draw"),
    AWAY_TEAM(3L, "Away win");

    private static final String UNKNOWN_COMPETITION_RESULT_ID_MSG = "Unknown competition result ID: %s";
    private static final String UNKNOWN_COMPETITION_RESULT_NAME_MSG = "Unknown competition result name: %s";

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

    public static CompetitionResult resolveCompetitionResultById(Long id) throws UnknownEnumAttributeException {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getId().equals(id)) {
                return competitionResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_COMPETITION_RESULT_ID_MSG, id));
    }

    public static CompetitionResult resolveCompetitionResultByName(String name) throws UnknownEnumAttributeException {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getName().equals(name)) {
                return competitionResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_COMPETITION_RESULT_NAME_MSG, name));
    }

}