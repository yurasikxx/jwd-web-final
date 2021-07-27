package com.epam.jwd.model;

public enum BetResult implements BaseEntity {

    WIN(1L, "Win"),
    LOSS(2L, "Loss");

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

    public static BetResult resolveBetResultById(Long id) {
        for (BetResult betResult : values()) {
            if (betResult.getId().equals(id)) {
                return betResult;
            }
        }

        throw new IllegalArgumentException("Unknown bet result ID");
    }

    public static BetResult resolveBetResultByName(String name) {
        for (BetResult betResult : values()) {
            if (betResult.getName().equals(name)) {
                return betResult;
            }
        }

        throw new IllegalArgumentException("Unknown bet result name");
    }

}