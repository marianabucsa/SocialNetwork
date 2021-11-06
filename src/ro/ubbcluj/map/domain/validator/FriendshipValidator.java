package ro.ubbcluj.map.domain.validator;

import ro.ubbcluj.map.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    /**
     * validates a friendship
     * @param entity - object to be validated
     * @throws ValidatorException - if the friendship is not valid
     */
    @Override
    public void validate(Friendship entity) throws ValidatorException {
        String err = "";
        if (entity.getPair().getId1() < 1)
            err += "Incorrect id!\n";
        if (entity.getPair().getId2() < 1)
            err += "Incorrect id!\n";
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
