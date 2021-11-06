package ro.ubbcluj.map.repository.memory;


import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.AbstractRepository;
import ro.ubbcluj.map.utils.Pair;

public class FriendshipsMemoryRepository extends AbstractRepository<Pair, Friendship> {

    /**
     * repository constructor
     *
     * @param validator - validator for entities of type E
     */
    public FriendshipsMemoryRepository(Validator<Friendship> validator) {
        super(validator);
    }
}
