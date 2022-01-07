package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class MessageSearchController extends UserMessageController{
    String workingUser;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    public MessageSearchController() throws IOException {
    }

    @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setUserController(user,currentUser, service);
        workingUser = user.getEmail();
        userFirstLastName.setText(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public void setMessageController(MessageDto message, String currentUser, Service service){
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setMessageController(message,currentUser, service);
        workingUser = service.getEmailFromId(message.getFrom());
        ArrayList<String> to = new ArrayList<>();
        for ( Long id: message.getTo()){
            User u = service.findOneUser(id);
            String name = u.getFirstName()+" "+u.getLastName();
            to.add(name);
        }
        User current_us = service.findOneUser(message.getFrom());
        userFirstLastName.setText(current_us.getFirstName()+" "+current_us.getLastName() + " " +to);
    }
}
