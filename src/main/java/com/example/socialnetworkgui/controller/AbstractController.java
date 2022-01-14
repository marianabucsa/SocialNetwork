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


    public void setAbstractController(String currentUser, Service service) {
        this.currentUser = currentUser;
        this.service = service;
    }
}
