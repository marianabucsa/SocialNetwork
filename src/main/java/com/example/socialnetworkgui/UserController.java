package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.FriendRequestsController;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



public class UserController {
    String currentUser;
    Service service;
    ObservableList<UserDto> usersList = FXCollections.observableArrayList();

    @FXML
    private Label errorLabelText;

    @FXML
    private TableView<UserDto> friendsTable;

    @FXML
    private TableColumn<UserDto, String> columnFriendFirstName;

    @FXML
    private TableColumn<UserDto, String> columnFriendLastName;

    @FXML
    private TableColumn<UserDto, String> columnFriendEmail;
    @FXML
    private TextField textSearchByName;

    public void setService(Service service,String userEmail) {
        this.service = service;
        this.currentUser=userEmail;
    }

    private List<UserDto> getFriendsList() {
        List<UserDto> friends = service.findFriends(service.getIdFromEmail(currentUser));
        if (friends.size() == 0)
            throw new ServiceException("No friends found!");
        return friends;
    }

    private List<UserDto> getUsersList() {
        List<UserDto> users = StreamSupport.stream(service.findAllUsers().spliterator(), false)
                .map(n -> new UserDto(n.getFirstName(), n.getLastName(), n.getEmail()))
                .collect(Collectors.toList());
        if (users.size() == 0)
            throw new ServiceException("No user with that name!");
        return users;
    }

    private void nameFilter() {
        if (Objects.equals(textSearchByName.getText(), ""))
            usersList.clear();
        else {
            List<String> names = List.of(textSearchByName.getText().split(" "));
            for (String name : names) {
                Predicate<UserDto> p1 = n -> n.getFirstName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p2 = n -> n.getLastName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p3 = n -> n.getFirstName().startsWith(name.toLowerCase());
                Predicate<UserDto> p4 = n -> n.getLastName().startsWith(name.toLowerCase());
                usersList.setAll(getUsersList().stream()
                        .filter(p1.or(p2).or(p3).or(p4)).collect(Collectors.toList()));
            }
        }
    }

    @FXML
    private void initialize() {
        columnFriendFirstName.setCellValueFactory(new PropertyValueFactory<UserDto, String>("FirstName"));
        columnFriendLastName.setCellValueFactory(new PropertyValueFactory<UserDto, String>("LastName"));
        columnFriendEmail.setCellValueFactory(new PropertyValueFactory<UserDto, String>("Email"));

        friendsTable.setItems(usersList);

        textSearchByName.textProperty().addListener(o -> nameFilter());
    }


    @FXML
    protected void onAddFriendClick() {
        try {
            UserDto selectedUser = (UserDto) friendsTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                service.addFriendship(currentUser, selectedUser.getEmail());
                errorLabelText.setAlignment(Pos.CENTER);
                errorLabelText.setTextFill(Paint.valueOf("green"));
                errorLabelText.setText("Friend request sent!");
            } else {
                errorLabelText.setAlignment(Pos.CENTER);
                errorLabelText.setTextFill(Paint.valueOf("red"));
                errorLabelText.setText("Please select a user!");
            }
        } catch (ValidatorException ve) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ime.getMessage());
        }
    }

    @FXML
    protected void onDeleteFriendClick() {
        try {
            UserDto selectedUser = (UserDto) friendsTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                service.deleteFriendship(currentUser, selectedUser.getEmail());
                errorLabelText.setAlignment(Pos.CENTER);
                errorLabelText.setTextFill(Paint.valueOf("green"));
                errorLabelText.setText("Friend/Friend request deleted!");
            } else {
                errorLabelText.setAlignment(Pos.CENTER);
                errorLabelText.setTextFill(Paint.valueOf("red"));
                errorLabelText.setText("Please select a user!");
            }
        } catch (ValidatorException ve) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ime.getMessage());
        }
    }

    @FXML
    protected void onShowFriendsClick() {
        try {
            usersList.setAll(getFriendsList());
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("green"));
            errorLabelText.setText("Friends shown above!");
        } catch (ValidatorException ve) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorLabelText.setAlignment(Pos.CENTER);
            errorLabelText.setTextFill(Paint.valueOf("red"));
            errorLabelText.setText(ime.getMessage());
        }
    }

    @FXML
    protected void onFriendRequestsClick() {
        showFriendRequestsView();
    }

    public void showFriendRequestsView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/friend-requests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            FriendRequestsController friendRequestsController = loader.getController();
            friendRequestsController.setCurrentUser(currentUser);
            friendRequestsController.setService(service);

            Stage friendRequestsStage = new Stage();
            Scene scene = new Scene(root, 651, 400);
            friendRequestsStage.setScene(scene);
            friendRequestsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
