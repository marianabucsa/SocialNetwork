package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.io.IOException;

public abstract class AbstractController implements Observer<ServiceEvent> {
    protected String currentUser;
    protected Service service;

    public void setAbstractController(String currentUser, Service service) {
        this.currentUser = currentUser;
        this.service = service;
    }
}
