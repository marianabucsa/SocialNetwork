package ro.ubbcluj.map.domain.validator;

/**
 * own class of exceptions
 * ValidationException extended from RuntimeException
 */
public class ValidatorException extends RuntimeException {
    /**
     * constructor for a Validator Exception
     * @param err - string of the error message
     */
    public ValidatorException(String err) {
        super(err);
    }
}
