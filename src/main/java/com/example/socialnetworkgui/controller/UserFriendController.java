package com.example.socialnetworkgui.controller;

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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.InputMismatchException;

public class UserFriendController extends UserUsersController {
    String workingUser;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    public void onDeleteFriendClick(ActionEvent actionEvent) {
        try {
            service.deleteFriendship(currentUser, workingUser);
            User user = service.findOneUser(service.getIdFromEmail(workingUser));
            UserDto userDto = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
            service.notifyObservers(new ServiceEvent(EventType.DELETE_FRIEND,userDto));
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("green"));
            errorUserSearchLabel.setText("Friend deleted!");
        } catch (ValidatorException ve) {
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("red"));
            errorUserSearchLabel.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("red"));
            errorUserSearchLabel.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("red"));
            errorUserSearchLabel.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("red"));
            errorUserSearchLabel.setText(ime.getMessage());
        }
    }

    public void onSendMessageClick(ActionEvent actionEvent) {
        User user = service.findOneUser(service.getIdFromEmail(workingUser));
        UserDto userDto = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
        service.notifyObservers(new ServiceEvent(EventType.SENT_MESSAGE,userDto));
    }

    @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setUserUsersController(service,currentUser);
        workingUser = user.getEmail();
        userFirstLastName.setText(user.getFirstName() + " " + user.getLastName());
    }
}

