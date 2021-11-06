package ro.ubbcluj.map.repository.memory;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.AbstractRepository;

public class UserMemoryRepository extends AbstractRepository<Long, User> {

    /**
     * repository constructor
     *
     * @param validator - validator for entities of type E
     */
    public UserMemoryRepository(Validator validator) {
        super(validator);
    }

}
