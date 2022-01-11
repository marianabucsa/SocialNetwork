package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.io.IOException;

public abstract class AbstractController implements Observer<ServiceEvent> {
    protected String currentUser;
    protected Service service;
    protected UserDto workingUser;
    protected MessageDto workingMessage;

    public void setUserController(UserDto workingUser,String currentUser, Service service) {
        this.currentUser = currentUser;
        this.service = service;
        service.addObserver(this);
        this.workingUser=workingUser;
    }

    public void setMessageController(MessageDto workingMessage, String currentUser, Service service) throws IOException {
        this.currentUser = currentUser;
        this.service = service;
        service.addObserver(this);
        this.workingMessage = workingMessage;
    }

}
