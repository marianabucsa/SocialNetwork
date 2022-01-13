package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.ReplyMessage;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserUsersController extends AbstractFriendsController {
    protected ObservableList<UserDto> usersSearchList = FXCollections.observableArrayList();
    protected ObservableList<UserDto> usersFriendsList = FXCollections.observableArrayList();
    protected ObservableList<UserDto> usersSentRequestsList = FXCollections.observableArrayList();
    protected ObservableList<UserDto> usersReceivedRequestsList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> conversationList = FXCollections.observableArrayList();

    @FXML
    private TextField textSearchByName;
    @FXML
    private VBox usersVBox;

    @FXML
    private Pane paneMessages;
    @FXML
    private Button btnSend;
    @FXML
    private TextArea textMessage;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblErrors;

    public void setUserUsersController(Service service, String currentUser) {
        super.setUserController(null, currentUser, service);
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<UserDto> usersList) throws IOException {
        usersVBox.getChildren().clear();
        for (UserDto user : usersList) {
            usersVBox.getChildren().add(getSearchFormatView(user, formatURL));
        }
    }

    @FXML
    private void initVBox(URL formatURL, ObservableList<MessageDto> messageList) throws IOException {
        usersVBox.getChildren().clear();
        for(MessageDto message : messageList){
            usersVBox.getChildren().add(getConversationFormatView(message,formatURL));
        }
    }

    public void onSearchClick(ActionEvent actionEvent) {
        if (Objects.equals(textSearchByName.getText(), ""))
            usersSearchList.clear();
        else {
            List<String> names = List.of(textSearchByName.getText().split(" "));
            for (String name : names) {
                Predicate<UserDto> p1 = n -> n.getFirstName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p2 = n -> n.getLastName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p3 = n -> n.getFirstName().startsWith(name.toLowerCase());
                Predicate<UserDto> p4 = n -> n.getLastName().startsWith(name.toLowerCase());
                usersSearchList.setAll(getUsersList().stream()
                        .filter(p1.or(p2).or(p3).or(p4)).collect(Collectors.toList()));
            }
            try {
                initializeVBox(getUserSearchFormat(), usersSearchList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onShowFriendsClick(ActionEvent actionEvent) {
        try {
            usersFriendsList.setAll(getFriendsList());
            initializeVBox(getUserFriendFormat(), usersFriendsList);
        } catch (ValidatorException ve) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(e.getMessage()));
        }

    }

    public void onSentRequests(ActionEvent actionEvent) {
        try {
            usersSentRequestsList.setAll(getSentRequestsList());
            initializeVBox(getUserSentRequestsFormat(), usersSentRequestsList);
        } catch (ValidatorException ve) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(e.getMessage()));
        }

    }

    public void onReceivedRequestsClick(ActionEvent actionEvent) {
        try {
            usersReceivedRequestsList.setAll(getReceivedRequestsList());
            initializeVBox(getUserReceivedRequestsFormat(), usersReceivedRequestsList);
        } catch (ValidatorException ve) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(e.getMessage()));
        }

    }

    private List<UserDto> getReceivedRequestsList() {
        List<UserDto> friendRequests = service.findReceivedFriendRequests(service.getIdFromEmail(currentUser));
        if (friendRequests.size() == 0)
            throw new ServiceException("No friends requests found!");
        return friendRequests;
    }

    private List<UserDto> getSentRequestsList() {
        List<UserDto> friendRequests = service.findSentFriendRequests(service.getIdFromEmail(currentUser));
        if (friendRequests.size() == 0)
            throw new ServiceException("No friends requests found!");
        return friendRequests;
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

    private List<MessageDto> getConversationList(){
        List<MessageDto> msgs = service.showConversation(currentUser,workingUser.getEmail()).stream()
                .map(n -> new MessageDto(n.getFrom(),n.getTo(),n.getMessage(),n.getData()))
                .collect(Collectors.toList());
        if(msgs.size() == 0)
            throw new ServiceException("No messages found!");
        return msgs;

    }

    private java.net.URL getConversationFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/ConversationView.fxml");
    }

    private java.net.URL getUserFriendFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserFriendView.fxml");
    }

    private java.net.URL getUserReceivedRequestsFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/ReceivedRequestsView.fxml");
    }

    private java.net.URL getUserSentRequestsFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/SentRequestsView.fxml");
    }

    private java.net.URL getUserSearchFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserSearchView.fxml");
    }

    private AnchorPane getSearchFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractFriendsController userSearchController = searchUserViewLoader.getController();
        userSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    private AnchorPane getConversationFormatView(MessageDto message, URL formatURL) throws IOException {
        FXMLLoader conversationViewLoader = new FXMLLoader();
        conversationViewLoader.setLocation(formatURL);
        AnchorPane conversationView = new AnchorPane();
        conversationView = conversationViewLoader.load();
        AbstractMessagesController conversationController = conversationViewLoader.getController();
        conversationController.setMessageController(message, currentUser, service);
        return conversationView;
    }

    private void showMessageElements(){
        paneMessages.setVisible(true);
        btnSend.setVisible(true);
        textMessage.setVisible(true);
        lblErrors.setVisible(true);
        lblUserName.setVisible(true);
    }

    private void hideMessageElements(){
        paneMessages.setVisible(false);
        btnSend.setVisible(false);
        textMessage.setVisible(false);
        lblErrors.setVisible(false);
        lblUserName.setVisible(false);

    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {
        switch (serviceEvent.getEventType()) {
            case ADD_FRIEND: {
                try {
                    usersSentRequestsList.setAll(getSentRequestsList());
                    initializeVBox(getUserSentRequestsFormat(), usersSentRequestsList);
                    break;
                } catch (ValidatorException ve) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(e.getMessage()));
                }
            }
            case DELETE_FRIEND: {
                try {
                    usersFriendsList.setAll(getFriendsList());
                    initializeVBox(getUserFriendFormat(), usersFriendsList);
                    break;
                } catch (ValidatorException ve) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(e.getMessage()));
                }
            }
            case ACCEPT_FRIENDSHIP:
            case DECLINE_FRIENDSHIP: {
                try {
                    usersReceivedRequestsList.setAll(getReceivedRequestsList());
                    initializeVBox(getUserReceivedRequestsFormat(), usersReceivedRequestsList);
                    break;
                } catch (ValidatorException ve) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(e.getMessage()));
                }
            }
            case CANCEL_FRIENDSHIP: {
                try {
                    usersSentRequestsList.setAll(getSentRequestsList());
                    initializeVBox(getUserFriendFormat(), usersFriendsList);
                    break;
                } catch (ValidatorException ve) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(e.getMessage()));
                }
            }
            case SENT_MESSAGE: {
                try{
                    workingUser = (UserDto) serviceEvent.getData();
                    showMessageElements();
                    wait(3000);
                    hideMessageElements();

                    //System.out.println(workingUser);
                    //conversationList.setAll(getConversationList());
                    //initVBox(getConversationFormat(), conversationList);
                }catch (ValidatorException | InterruptedException ve){
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                }
            }
        }
    }
}
