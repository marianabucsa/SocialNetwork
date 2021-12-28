package com.example.socialnetworkgui.domain.validator;

public interface Validator<T> {
    /**
     * validates an entity
     *
     * @param entity - an object
     * @throws ValidatorException - if the entity is not valid
     */
    void validate(T entity) throws ValidatorException;
}
