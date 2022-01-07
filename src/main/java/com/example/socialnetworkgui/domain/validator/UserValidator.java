package com.example.socialnetworkgui.domain.validator;

import com.example.socialnetworkgui.domain.User;

public class UserValidator implements Validator<User> {
    /**
     * validates a user
     *
     * @param entity - user to be validated
     * @throws ValidatorException if the id, first name, last name or email of the user is not valid
     */
    @Override
    public void validate(User entity) throws ValidatorException {
        String err = "";
        if (!entity.getFirstName().matches("^[A-Za-z]+")) {
            err += "Incorrect first name!\n";
        }
        if (!entity.getLastName().matches("^[A-Za-z]+")) {
            err += "Incorrect last name!\n";
        }
        if (!entity.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            err += "Invalid email!\n";
        }
        if (entity.getPassword().matches("")) {
            err += "Invalid password!\n";
        }
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
