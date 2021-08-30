package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

/**
 * Enum {@code CompetitionResult} is type of sport. Implements {@code BaseEntity} interface.
 *
 * @see Team
 */
public enum Sport implements BaseEntity {

    BASKETBALL(1L, "Basketball"),
    FOOTBALL(2L, "Football"),
    HOCKEY(3L, "Hockey");

    private static final String UNKNOWN_SPORT_ID_MSG = "Unknown sport ID: %s";
    private static final String UNKNOWN_SPORT_NAME_MSG = "Unknown sport name: %s";

    private final Long id;
    private final String name;

    Sport(Long id, String name) {
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

    public static Sport resolveSportById(Long id) throws UnknownEnumAttributeException {

        for (Sport sport : values()) {
            if (sport.getId().equals(id)) {
                return sport;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_SPORT_ID_MSG, id));
    }

    public static Sport resolveSportByName(String name) throws UnknownEnumAttributeException {
        for (Sport sport : values()) {
            if (sport.getName().equals(name)) {
                return sport;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_SPORT_NAME_MSG, name));
    }

}