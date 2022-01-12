package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class UserMyEventController extends UserMyEventsController{
    @FXML
    private Label nameLabel;
    @FXML
    private Label startDateTimeLabel;
    @FXML
    private Label endDateTimeLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label descriptionLabel;




    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

    @Override
    public void setAbstractEventController(String currentUser, Service service, Event event) {
        super.setAbstractEventController(currentUser,service,event);
        nameLabel.setText(event.getName());
        startDateTimeLabel.setText(String.valueOf(event.getStartDate()));
        endDateTimeLabel.setText(String.valueOf(event.getEndDate()));
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText(event.getLocation());
    }

    public void onModifyClicked(ActionEvent actionEvent) {
    }

    public void onDeleteClicked(ActionEvent actionEvent) {
    }
}
