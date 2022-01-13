package com.example.socialnetworkgui.domain.validator;

import com.example.socialnetworkgui.domain.Event;

public class EventValidator implements Validator<Event>{
    @Override
    public void validate(Event entity) throws ValidatorException {
        String err = "";
        if (entity.getName().matches("")) {
            err += "Event must have a name!\n";
        }
        if (entity.getLocation().matches("")) {
            err += "Event must have a location!\n";
        }
        if (entity.getDescription().matches("")) {
            err += "Event must have a description!\n";
        }
        if (entity.getStartDate()==null) {
            err += "Event must have a start date!\n";
        }
        if (entity.getEndDate()==null) {
            err += "Event must have an end date!\n";
        }
        if (entity.getStartDate().isAfter(entity.getEndDate())) {
            err += "The start date is after the end date!\n";
        }
        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
