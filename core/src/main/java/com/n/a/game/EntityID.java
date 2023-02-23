package com.n.a.game;

import java.util.Objects;

/**
 * An Entity ID for referencable objects. This is used for
 * the data layer belonging to the game world (LibGDX Actors/Graphics)
 */
public class EntityID {

    private String id;

    protected EntityID(String id) {
        this.set(id);
    }

    public String get() {
        return id;
    }

    protected void set(String id) {
        if( id == null) throw new NullPointerException("ID cannot be null");
        this.id = id;
    }

    /**
     * Convenience method.
     * @param id
     * @return if provided id is the same as the id inside of this entityID
     */
    public boolean equalsTo(String id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityID entityID = (EntityID) o;
        return Objects.equals(id, entityID.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EntityID{" +
                "id='" + id + '\'' +
                '}';
    }
}
