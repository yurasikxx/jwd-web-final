package com.epam.jwd.model;

public enum BetType implements BaseEntity {

    HOME_TEAM_WIN(1L, "Home team win"),
    AWAY_TEAM_WIN(2L, "Away team win"),
    DRAW(3L, "Draw"),
    HOME_TEAM_WILL_NOT_LOSE(4L, "Home team won't lose"),
    AWAY_TEAM_WILL_NOT_LOSE(5L, "Away team won't lose"),
    NO_DRAW(6L, "No draw");

    private final Long id;
    private final String name;

    BetType(Long id, String name) {
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

    public static BetType resolveBetTypeById(Long id) {
        for (BetType betType : values()) {
            if (betType.getId().equals(id)) {
                return betType;
            }
        }

        throw new IllegalArgumentException("Unknown bet type ID");
    }

    public static BetType resolveBetTypeByName(String name) {
        for (BetType betType : values()) {
            if (betType.getName().equals(name)) {
                {
                    return betType;
                }
            }
        }

        throw new IllegalArgumentException("Unknown bet type name");
    }

}