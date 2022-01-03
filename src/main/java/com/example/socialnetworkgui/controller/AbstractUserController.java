package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;

public abstract class AbstractUserController {
    String currentUser;
    Service service;
    UserDto workingUser;

    public void setUserController(UserDto workingUser,String currentUser, Service service) {
        this.currentUser = currentUser;
        this.service = service;
        this.workingUser=workingUser;
    }

}
