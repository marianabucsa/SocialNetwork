package com.example.socialnetworkgui.domain;

import java.util.Objects;


public class Entity<ID> {

    private ID id;

    /**
     * getter methode for id
     *
     * @return - the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * setter methode for id
     *
     * @param id - the id of the entity
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * verifies if 2 object are equal
     *
     * @param o - an object
     * @return - true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    /**
     * gives hash code for an object
     *
     * @return - a hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
