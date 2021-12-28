package com.example.socialnetworkgui.repository.memory;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.AbstractRepository;

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
