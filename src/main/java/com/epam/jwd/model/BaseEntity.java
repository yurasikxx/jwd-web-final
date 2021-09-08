package com.epam.jwd.model;

/**
 * Interface {@code BaseEntity} is the root of application models.
 * Every model has {@code BaseEntity} as a superinterface.
 *
 * @see AbstractBaseEntity
 */
public interface BaseEntity {

    /**
     * @return an entity ID.
     */
    Long getId();

}