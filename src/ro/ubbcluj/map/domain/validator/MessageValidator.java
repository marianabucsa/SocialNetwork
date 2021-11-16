package ro.ubbcluj.map.domain.validator;

import ro.ubbcluj.map.domain.ReplyMessage;

public class MessageValidator implements Validator<ReplyMessage> {
    @Override
    public void validate(ReplyMessage entity) throws ValidatorException {
        String err = "";
        if (entity.getId()<1) {
            err += "Incorrect id!\n";
        }
        if (entity.getFrom()<0) {
            err += "Incorrect user id!\n";
        }
        for(Long id : entity.getTo()) {
            if(id<0)
                err += "Incorrect user id!\n";
        }
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
