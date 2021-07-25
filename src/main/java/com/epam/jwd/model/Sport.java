package com.epam.jwd.model;

public enum Sport implements BaseEntity {

    BASKETBALL(1L, "Basketball"),
    FOOTBALL(2L, "Football"),
    HOCKEY(3L, "Hockey");

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

    public static Sport resolveSportById(Long id) {
        for (Sport sport : values()) {
            if (sport.getId().equals(id)) {
                return sport;
            }
        }

        throw new IllegalArgumentException("Unknown sport ID");
    }

    public static Sport resolveSportByName(String name) {
        for (Sport sport : values()) {
            if (sport.getName().equals(name)) {
                return sport;
            }
        }

        throw new IllegalArgumentException("Unknown sport name");
    }

}