package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.io.IOException;

public abstract class AbstractEventsController extends AbstractController {
    protected Event workingEvent;

    public void setAbstractEventController(String currentUser, Service service,Event event) {
        super.setAbstractController(currentUser, service);
        service.addObserver(this);
        this.workingEvent=event;
    }

}
