package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.FriendshipDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.Pair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestsController {
    Service service;
    String currentUser;
    ObservableList<FriendshipDto> model = FXCollections.observableArrayList();

    @FXML
    TableView<FriendshipDto> tableView;
    @FXML
    TableColumn<FriendshipDto, String> tableColumnFirstName;
    @FXML
    TableColumn<FriendshipDto, String> tableColumnLastName;
    @FXML
    TableColumn<FriendshipDto, String> tableColumnStatus;
    @FXML
    TableColumn<FriendshipDto, String> tableColumnDate;

    public void setService(Service service) {
        this.service = service;
        model.setAll(getFriendshipDtoList());
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableView.setItems(model);
    }

    private List<FriendshipDto> getFriendshipDtoList() {
        return service.findFriendRequests(service.getIdFromEmail(currentUser))
                .stream()
                .map(x -> new FriendshipDto(x,                                                       //idFrom
                        service.getIdFromEmail(currentUser),                                         //idTo
                        service.findOneUser(x).getFirstName(),                                       //firstName
                        service.findOneUser(x).getLastName(),                                        //lastName
                        service.FriendshipStatus(x, service.getIdFromEmail(currentUser)),            //status
                        service.FriendshipDate(x, service.getIdFromEmail(currentUser)).toString()))  //date
                .collect(Collectors.toList());
    }

    @FXML
    public void handleAccept() {
        FriendshipDto selectedRequest = tableView.getSelectionModel().getSelectedItem();
        if(selectedRequest != null) {
            try {
                if(service.updateFriendship(new Pair(selectedRequest.getIdTo(), selectedRequest.getIdFrom()), "approved") == null)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship", "Friendship approved");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship", "Could not accept friendship");
            } catch (SecurityException e) {
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No friend request selected");

        model.setAll(getFriendshipDtoList());
        tableView.setItems(model);
    }

    @FXML
    public void handleReject() {
        FriendshipDto selectedRequest = tableView.getSelectionModel().getSelectedItem();
        if(selectedRequest != null) {
            try {
                if(service.updateFriendship(new Pair(selectedRequest.getIdTo(), selectedRequest.getIdFrom()), "rejected") == null)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship", "Friendship rejected");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship", "Could not reject friendship");
            } catch (SecurityException e) {
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No friend request selected");

        model.setAll(getFriendshipDtoList());
        tableView.setItems(model);
    }
}
