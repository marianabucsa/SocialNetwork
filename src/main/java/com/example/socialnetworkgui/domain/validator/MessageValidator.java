package com.example.socialnetworkgui.domain.validator;

import com.example.socialnetworkgui.domain.ReplyMessage;

public class MessageValidator implements Validator<ReplyMessage> {
    /**
     * validates a message
     *
     * @param entity - object to be validated
     * @throws ValidatorException - if the message is not valid
     */
    @Override
    public void validate(ReplyMessage entity) throws ValidatorException {
        String err = "";
        if (entity.getFrom() < 0) {
            err += "Incorrect user id!\n";
        }
        for (Long id : entity.getTo()) {
            if (id < 0)
                err += "Incorrect user id!\n";
        }
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
