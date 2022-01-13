package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class UserShowParticipantsController extends AbstractEventsController {
    protected ObservableList<UserDto> participantsList = FXCollections.observableArrayList();
    @FXML
    private VBox participantsVBox;

    @Override
    public void setAbstractEventController(String currentUser, Service service, Event event) {
        super.setAbstractEventController(currentUser, service,event);
        try {
            participantsList.setAll(getParticipantsList());
            initializeVBox(getParticipantFormat(), participantsList);
        } catch (ValidatorException ve) {
            participantsVBox.getChildren().clear();
            participantsVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            participantsVBox.getChildren().clear();
            participantsVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            participantsVBox.getChildren().clear();
            participantsVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            participantsVBox.getChildren().clear();
            participantsVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            participantsVBox.getChildren().clear();
            participantsVBox.getChildren().add(new Text(e.getMessage()));
        }
    }

    private AnchorPane getParticipantFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractFriendsController userSearchController = searchUserViewLoader.getController();
        userSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    private URL getParticipantFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserSearchView.fxml");
    }

    private List<UserDto> getParticipantsList() {
        List<Long> part = workingEvent.getParticipants();
        if (part != null) {
            List<UserDto> users = new ArrayList<>();
            for (Long id : part) {
                User us = service.findOneUser(id);
                UserDto user = new UserDto(us.getFirstName(), us.getLastName(), us.getEmail());
                users.add(user);
            }
            return users;
        } else {
            throw new ServiceException("No participants yet!");
        }
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<UserDto> participantsList) throws IOException {
        participantsVBox.getChildren().clear();
        for (UserDto user : participantsList) {
            participantsVBox.getChildren().add(getParticipantFormatView(user, formatURL));
        }
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) participantsVBox.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
