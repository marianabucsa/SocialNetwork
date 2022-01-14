package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.EventType;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class MessageSearchController extends UserMessageController{
    String workingUser;
    MessageDto message;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    public MessageSearchController() throws IOException {
    }

    @Override
    public void setMessageController(MessageDto message, String currentUser, Service service) {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setMessageController(message,currentUser, service);
        super.setAbstractController(currentUser,service);
        workingUser = service.getEmailFromId(message.getFrom());
        this.message = message;
        ArrayList<String> to = new ArrayList<>();

        User current_user = service.findOneUser(service.getIdFromEmail(currentUser));
        String current_user_name = current_user.getFirstName()+" "+current_user.getLastName();

        for ( Long id: message.getTo()){
            User u = service.findOneUser(id);
            String name = u.getFirstName()+" "+u.getLastName();
            if(!name.equals(current_user_name)) {
                String name2 = u.getFirstName();

                to.add(name2);
            }
        }
        User current_us = service.findOneUser(message.getFrom());
        String name = current_us.getFirstName()+" "+current_us.getLastName();
        if(!name.equals(current_user_name)) {
            userFirstLastName.setText(current_us.getFirstName());
            for (String s:to) {
                userFirstLastName.setText(userFirstLastName.getText()+" "+s);
            }
        }
        else
        {
            userFirstLastName.setText(to.toString());
        }
    }

    @FXML
    private void onSendMessageClick() throws IOException {
        service.notifyObservers(new ServiceEvent(EventType.SEND_MESSAGE, message));
    }
}
