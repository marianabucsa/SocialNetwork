package ro.ubbcluj.map.repository.DB;


import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.Repository;

public abstract class AbstractRepoDatabase<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected String url;
    protected String username;
    protected String password;
    protected Validator<E> validator;

    /**
     * repository constructor
     * @param url - database information
     * @param username - database information
     * @param password - database information
     * @param validator - database information
     */
    public AbstractRepoDatabase(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
}
