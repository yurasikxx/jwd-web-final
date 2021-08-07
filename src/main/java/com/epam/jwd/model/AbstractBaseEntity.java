package com.epam.jwd.model;

import java.util.Objects;

/**
 * Abstract class {@code AbstractBaseEntity} is layer
 * between {@code BaseEntity} interface and other application models.
 *
 * @see BaseEntity
 */
public abstract class AbstractBaseEntity implements BaseEntity {

    private final Long id;

    /**
     * An {@code AbstractBaseEntity} constructor.
     *
     * @param id entity ID.
     */
    public AbstractBaseEntity(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AbstractBaseEntity{" +
                "id=" + id +
                '}';
    }

}