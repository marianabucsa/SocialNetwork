package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.service.Service;

public abstract class AbstractMessagesController extends AbstractController {
    protected MessageDto workingMessage;

    public void setMessageController(MessageDto workingMessage,String currentUser, Service service) {
        super.setAbstractController(currentUser,service);
        service.addObserver(this);
        this.workingMessage=workingMessage;
    }
}
