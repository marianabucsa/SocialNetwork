package com.example.socialnetworkgui.domain.validator;

public class EmailValidator implements Validator<String> {

    /**
     * validates an email
     * @param entity - a string
     * @throws ValidatorException - if email does not have the right format
     */
    @Override
    public void validate(String entity) throws ValidatorException {
        String err="";
        if (!entity.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            err += "Invalid email!\n";
        }
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
