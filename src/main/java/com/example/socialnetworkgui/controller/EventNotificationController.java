package com.example.socialnetworkgui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EventNotificationController {
    @FXML
    private Label nameLabel;

    public void setEventNotificationController(String name){
        nameLabel.setText(name);
    }
    public void onOKAction(ActionEvent actionEvent) {
        Stage stage= (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }
}
