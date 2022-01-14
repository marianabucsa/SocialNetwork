package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.EventType;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

public class UserMyEventController extends UserEventsController{
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
    @FXML
    private Label errorLabel;



    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {
        switch (serviceEvent.getEventType()) {
            case DELETE_EVENT,
                MODIFY_EVENT:{
                try {
                    userMyEventsController.myEventsList.setAll(userMyEventsController.getMyEventsList());
                    userMyEventsController.initializeVBox(userMyEventsController.getMyEventFormat(), userMyEventsController.myEventsList);
                } catch (ValidatorException ve) {
                    userMyEventsController.eventsVBox.getChildren().clear();
                    Text text= new Text(ve.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ve.getMessage());
                    userMyEventsController.eventsVBox.getChildren().add(text);
                    userMyEventsController.eventsVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (ServiceException se) {
                    userMyEventsController.eventsVBox.getChildren().clear();
                    Text text= new Text(se.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(se.getMessage());
                    userMyEventsController.eventsVBox.getChildren().add(text);
                    userMyEventsController.eventsVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (RepositoryException re) {
                    userMyEventsController.eventsVBox.getChildren().clear();
                    Text text= new Text(re.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(re.getMessage());
                    userMyEventsController.eventsVBox.getChildren().add(text);
                    userMyEventsController.eventsVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (InputMismatchException ime) {
                    userMyEventsController.eventsVBox.getChildren().clear();
                    Text text= new Text(ime.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ime.getMessage());
                    userMyEventsController.eventsVBox.getChildren().add(text);
                    userMyEventsController.eventsVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (IOException e) {
                    userMyEventsController.eventsVBox.getChildren().clear();
                    Text text= new Text(e.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(e.getMessage());
                    userMyEventsController.eventsVBox.getChildren().add(text);
                    userMyEventsController.eventsVBox.setAlignment(Pos.valueOf("CENTER"));
                }
            }

        }

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
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/UserModifyMyEventView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserEventsController abstractController = fxmlLoader.getController();
        abstractController.setAbstractEventController(currentUser, service, workingEvent);
        abstractController.setUserMyEventsController(this.userMyEventsController);
        Stage userStage = new Stage();
        Scene scene = new Scene(root);
        userStage.initStyle(StageStyle.TRANSPARENT);
        userStage.setScene(scene);
        userStage.show();
    }

    public void onDeleteClicked(ActionEvent actionEvent) {
        try {
            service.deleteEvent(workingEvent);
            service.notifyObservers(new ServiceEvent(EventType.DELETE_EVENT,workingEvent));
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("green"));
            errorLabel.setText("Event deleted!");
        } catch (ValidatorException ve) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(ime.getMessage());
        }
    }

    public void onShowParticipantsClicked(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/UserShowParticipantsView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AbstractEventsController abstractController = fxmlLoader.getController();
        abstractController.setAbstractEventController(currentUser, this.service,workingEvent);

        Stage userStage = new Stage();
        Scene scene = new Scene(root);
        userStage.initStyle(StageStyle.TRANSPARENT);
        userStage.setScene(scene);
        userStage.show();
    }
}
