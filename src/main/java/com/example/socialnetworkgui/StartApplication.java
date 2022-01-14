package com.example.socialnetworkgui;


import com.example.socialnetworkgui.controller.StartupController;
import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.*;
import com.example.socialnetworkgui.repository.DB.EventsDBRepository;
import com.example.socialnetworkgui.repository.DB.FriendshipsDBRepository;
import com.example.socialnetworkgui.repository.DB.MessagesDBRepository;
import com.example.socialnetworkgui.repository.DB.UserDBRepository;
import com.example.socialnetworkgui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.time.LocalDate.now;


public class StartApplication extends Application{

    UserDBRepository userDBRepository;
    FriendshipsDBRepository friendshipsDBRepository;
    MessagesDBRepository messagesDBRepository;
    EventsDBRepository eventsDBRepository;
    Service service;

    public static void main(String[] args)  {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();

        EventValidator eventValidator= new EventValidator();
        userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/Network","postgres","luceafarul1",userValidator);
        friendshipsDBRepository = new FriendshipsDBRepository("jdbc:postgresql://localhost:5432/Network","postgres","luceafarul1",friendshipValidator);
        messagesDBRepository = new MessagesDBRepository("jdbc:postgresql://localhost:5432/Network","postgres","luceafarul1",messageValidator);
        eventsDBRepository=new EventsDBRepository("jdbc:postgresql://localhost:5432/Network","postgres","luceafarul1",eventValidator);
        service = new Service(friendshipsDBRepository,userDBRepository,messagesDBRepository,eventsDBRepository, new EmailValidator());

        initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader startupLoader = new FXMLLoader();
        startupLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/startup-view.fxml"));
        AnchorPane startTaskLayout = startupLoader.load();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(startTaskLayout));

        StartupController startupController = startupLoader.getController();
        startupController.setService(service);

        primaryStage.setResizable(false);

    }
}
