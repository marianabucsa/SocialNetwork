package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.UserController;
import com.example.socialnetworkgui.domain.validator.EmailValidator;
import com.example.socialnetworkgui.domain.validator.FriendshipValidator;
import com.example.socialnetworkgui.domain.validator.MessageValidator;
import com.example.socialnetworkgui.domain.validator.UserValidator;
import com.example.socialnetworkgui.repository.DB.FriendshipsDBRepository;
import com.example.socialnetworkgui.repository.DB.MessagesDBRepository;
import com.example.socialnetworkgui.repository.DB.UserDBRepository;
import com.example.socialnetworkgui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserApplication extends Application {
    @Override
    public void start(Stage frontStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UserView.fxml"));
        AnchorPane root=fxmlLoader.load();

        UserController userController = fxmlLoader.getController();
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();
        EmailValidator emailValidator = new EmailValidator();
        UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", userValidator);
        FriendshipsDBRepository friendshipsDBRepository = new FriendshipsDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", friendshipValidator);
        MessagesDBRepository messagesDBRepository = new MessagesDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", messageValidator);
        userController.setService(new Service(friendshipsDBRepository, userDBRepository, messagesDBRepository, emailValidator));

        Scene scene = new Scene(root, 600, 400);
        frontStage.setScene(scene);
        frontStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
