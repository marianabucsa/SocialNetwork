package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

public abstract class AbstractController implements Observer<ServiceEvent> {
    protected String currentUser;
    protected Service service;
    protected UserDto workingUser;

    public void setUserController(UserDto workingUser,String currentUser, Service service) {
        this.currentUser = currentUser;
        this.service = service;
        service.addObserver(this);
        this.workingUser=workingUser;
    }

}
