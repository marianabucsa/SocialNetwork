package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConversationController extends UserMessageController{

    String workingUser;
    MessageDto message;

    @FXML
    private VBox usersVBox;
    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    @FXML
    private Label messageDate;

    @FXML
    private Label messageText;

    @FXML
    private Label messageSender;

    @Override
    public void setMessageController(MessageDto message, String currentUser, Service service) throws IOException {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setMessageController(message,currentUser, service);
        workingUser = service.getEmailFromId(message.getFrom());
        this.message = message;
        messageDate.setText(message.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy   hh:mm")));
        messageSender.setText(service.getEmailFromId(message.getFrom()));
        messageText.setText(message.getMessage());
    }


    /*@Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }*/
}
