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

    /**
     * A {@code CompetitionResult} constructor.
     *
     * @param id   a competition result ID.
     * @param name a competition result name.
     */
    CompetitionResult(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return a competition result name.
     */
    public String getName() {
        return name;
    }

    /**
     * Looking for an enum constant by its ID.
     *
     * @param id a competition result ID.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static CompetitionResult resolveCompetitionResultById(Long id) throws UnknownEnumAttributeException {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getId().equals(id)) {
                return competitionResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_COMPETITION_RESULT_ID_MSG, id));
    }

    /**
     * Looking for an enum constant by its name.
     *
     * @param name a competition result name.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static CompetitionResult resolveCompetitionResultByName(String name) throws UnknownEnumAttributeException {
        for (CompetitionResult competitionResult : values()) {
            if (competitionResult.getName().equals(name)) {
                return competitionResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_COMPETITION_RESULT_NAME_MSG, name));
    }

}