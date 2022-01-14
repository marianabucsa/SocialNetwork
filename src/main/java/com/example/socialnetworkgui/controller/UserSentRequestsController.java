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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.InputMismatchException;

public class UserSentRequestsController extends AbstractFriendsController {
    String workingUser;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setUserController(user,currentUser,service);
        workingUser = user.getEmail();
        userFirstLastName.setText(user.getFirstName() + " " + user.getLastName());
    }

    public void onCancelClick(ActionEvent actionEvent) {
        try {
            service.deleteFriendship(currentUser, workingUser);
            User user = service.findOneUser(service.getIdFromEmail(workingUser));
            UserDto userDto = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
            service.notifyObservers(new ServiceEvent(EventType.CANCEL_FRIENDSHIP,userDto));
            errorUserSearchLabel.setAlignment(Pos.CENTER);
            errorUserSearchLabel.setTextFill(Paint.valueOf("green"));
            errorUserSearchLabel.setText("Friend request canceled!");
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
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {
        switch (serviceEvent.getEventType()) {
            case ADD_FRIEND: {
                try {
                    userUsersController.usersSentRequestsList.setAll(userUsersController.getSentRequestsList());
                    userUsersController.initializeVBox(userUsersController.getUserSentRequestsFormat(), userUsersController.usersSentRequestsList);
                    break;
                } catch (ValidatorException ve) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ve.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ve.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (ServiceException se) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(se.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(se.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (RepositoryException re) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(re.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(re.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (InputMismatchException ime) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ime.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ime.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (IOException e) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(e.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(e.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                }
            }
            case DELETE_FRIEND: {
                try {
                    userUsersController.usersFriendsList.setAll(userUsersController.getFriendsList());
                    userUsersController.initializeVBox(userUsersController.getUserFriendFormat(), userUsersController.usersFriendsList);
                    break;
                } catch (ValidatorException ve) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ve.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ve.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (ServiceException se) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(se.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(se.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (RepositoryException re) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(re.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(re.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (InputMismatchException ime) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ime.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ime.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (IOException e) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(e.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(e.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                }
            }
            case ACCEPT_FRIENDSHIP:
            case DECLINE_FRIENDSHIP: {
                try {
                    userUsersController.usersReceivedRequestsList.setAll(userUsersController.getReceivedRequestsList());
                    userUsersController.initializeVBox(userUsersController.getUserReceivedRequestsFormat(), userUsersController.usersReceivedRequestsList);
                    break;
                } catch (ValidatorException ve) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ve.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ve.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (ServiceException se) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(se.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(se.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (RepositoryException re) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(re.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(re.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (InputMismatchException ime) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ime.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ime.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (IOException e) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(e.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(e.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                }
            }
            case CANCEL_FRIENDSHIP: {
                try {
                    userUsersController.usersSentRequestsList.setAll(userUsersController.getSentRequestsList());
                    userUsersController.initializeVBox(userUsersController.getUserFriendFormat(), userUsersController.usersFriendsList);
                    break;
                } catch (ValidatorException ve) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ve.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ve.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (ServiceException se) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(se.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(se.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (RepositoryException re) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(re.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(re.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (InputMismatchException ime) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(ime.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(ime.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                } catch (IOException e) {
                    userUsersController.usersVBox.getChildren().clear();
                    Text text= new Text(e.getMessage());
                    text.setFill(Color.valueOf("#B2B2B2"));
                    text.setText(e.getMessage());
                    userUsersController.usersVBox.getChildren().add(text);
                    userUsersController.usersVBox.setAlignment(Pos.valueOf("CENTER"));
                }
            }
        }

    }
}
