package com.example.socialnetworkgui.repository.memory;


import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.AbstractRepository;
import com.example.socialnetworkgui.utils.Pair;

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
