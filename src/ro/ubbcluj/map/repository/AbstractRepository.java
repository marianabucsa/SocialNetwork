package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validator.Validator;

import java.util.HashMap;

public abstract class AbstractRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Validator<E> validator;
    protected HashMap<ID, E> entities;

    /**
     * abstract repository constructor
     *
     * @param validator- validator for entities of type E
     */
    public AbstractRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new RepositoryException("Id must not be null!\n");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new RepositoryException("ID must not be null!");
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            entities.replace(entity.getId(), entity);
            return null;
        }
        return entity;

    }

    @Override
    public int size() {
        return entities.size();
    }

}
