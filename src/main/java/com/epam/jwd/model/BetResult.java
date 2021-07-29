package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

public enum BetResult implements BaseEntity {

    WIN(1L, "Win"),
    LOSS(2L, "Loss");

    private static final String UNKNOWN_BET_RESULT_ID_MSG = "Unknown bet result ID: %s";
    private static final String UNKNOWN_BET_RESULT_NAME_MSG = "Unknown bet result name: %s";
    private final Long id;
    private final String name;

    BetResult(Long id, String name) {
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

    public static BetResult resolveBetResultById(Long id) throws UnknownEnumAttributeException {
        for (BetResult betResult : values()) {
            if (betResult.getId().equals(id)) {
                return betResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_RESULT_ID_MSG, id));
    }

    public static BetResult resolveBetResultByName(String name) throws UnknownEnumAttributeException {
        for (BetResult betResult : values()) {
            if (betResult.getName().equals(name)) {
                return betResult;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_RESULT_NAME_MSG, name));
    }

}