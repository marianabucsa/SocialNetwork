package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MessagesController {
    String currentUser;
    Service service;
    ObservableList<UserDto> usersList = FXCollections.observableArrayList();

    @FXML
    private Button btnBack;

    public void setService(Service service,String userEmail) {
        this.service = service;
        this.currentUser=userEmail;
    }

    @FXML
    protected void onGoBackClick(){
        showUserView(currentUser);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    public void showUserView(String user)  {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UserView.fxml"));
        AnchorPane root= null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserController userController = fxmlLoader.getController();
        userController.setService(this.service,user);

        Stage userStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        userStage.setScene(scene);
        userStage.show();
    }
}
