package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validator.ValidatorException;

import java.util.HashMap;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws RepositoryException if id is null.
     */
    E findOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidatorException  if the entity is not valid
     * @throws RepositoryException if the given entity is null.     *
     */
    E save(E entity);


    /**
     * removes the entity with the specified id
     *
     * @param id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws RepositoryException if the given id is null.
     */
    E delete(ID id);

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     * @throws RepositoryException if the given entity is null.
     * @throws ValidatorException  if the entity is not valid.
     */
    E update(E entity);

    /**
     * getter for the size of the repository
     *
     * @return - a integer that represents the size of the repository
     */
    int size();

    /**
     * gets all data from repository
     * @return - a hash map
     */
    HashMap<ID,E> getAllData();

}
