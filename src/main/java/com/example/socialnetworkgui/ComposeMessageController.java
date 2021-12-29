package com.example.socialnetworkgui;

import com.example.socialnetworkgui.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ComposeMessageController {
    String currentUser;
    Service service;

    @FXML
    protected Label lblTo;
    @FXML
    protected Label lblCC;
    @FXML
    protected Label lblSubject;
    @FXML
    protected TextArea textArea;

    public void setService(Service service, String user) {
        this.service=service;
        this.currentUser=user;
    }

    @FXML
    protected void onCancelClick(){
        showMessagesView();
        Stage stage = (Stage) lblTo.getScene().getWindow();
        stage.close();
    }

    private void showMessagesView(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/messages-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            MessagesController messagesController = loader.getController();
            messagesController.setService(service,currentUser);

            Stage messageStage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            messageStage.setScene(scene);
            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
