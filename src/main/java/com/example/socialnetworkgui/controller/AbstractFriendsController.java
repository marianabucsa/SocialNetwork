package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

public abstract class AbstractFriendsController extends AbstractController {
    protected UserDto workingUser;

    public void setUserController(UserDto workingUser,String currentUser, Service service) {
        super.setAbstractController(currentUser,service);
        service.addObserver(this);
        this.workingUser=workingUser;
    }


}
