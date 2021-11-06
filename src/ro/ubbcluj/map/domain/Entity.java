package ro.ubbcluj.map.domain;

import java.io.Serializable;
import java.util.Objects;


public class Entity<ID> {

    private ID id;

    /**
     * getter methode for id
     * @return - the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * setter methode for id
     * @param id - the id of the entity
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
