package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserMessageController extends AbstractController{
    protected ObservableList<UserDto> usersSearchList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> messagesSearchList = FXCollections.observableArrayList();

    @FXML
    private TextField textSearchByName;
    @FXML
    private VBox usersVBox;


    /*public UserMessageController() throws IOException {
        super.setMessageController(null,currentUser,service);
        messagesSearchList.setAll(getMessagesList());

        initializeVBox(getUserSearchFormat(),messagesSearchList);
    }*/

    @Override
    public void setUserController(UserDto user, String currentUser, Service service){
        super.setUserController(null, currentUser, service);

    }

    @Override
    public void setMessageController(MessageDto user, String currentUser, Service service)  {
        super.setMessageController(null,currentUser,service);
        //messagesSearchList.setAll(getMessagesList());
       // initializeVBox(getUserSearchFormat(),messagesSearchList);
    }


    @FXML
    private void reinitializeVBox(URL formatURL, ObservableList<UserDto> usersList) throws IOException {
        usersVBox.getChildren().clear();
        for (UserDto user : usersList) {
            usersVBox.getChildren().add(getSearchUsersFormatView(user, formatURL));
        }
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<MessageDto> messagesList) throws IOException {
        //usersVBox.getChildren().clear();
        for (MessageDto message : messagesList){
            usersVBox.getChildren().add(getSearchMessagesFormatView(message, formatURL));
        }
    }

    public void onSearchClick(ActionEvent actionEvent) {
        if (Objects.equals(textSearchByName.getText(), "")) {
            usersSearchList.clear();
            messagesSearchList.clear();
        }
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
            for(String name:names){
                Predicate<MessageDto> p1 = n -> service.findOneUser(n.getFrom()).getFirstName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<MessageDto> p2 = n -> service.findOneUser(n.getFrom()).getLastName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<MessageDto> p3 = n -> service.findOneUser(n.getFrom()).getFirstName().startsWith(name.toLowerCase());
                Predicate<MessageDto> p4 = n -> service.findOneUser(n.getFrom()).getLastName().startsWith(name.toLowerCase());
                messagesSearchList.setAll(getMessagesList().stream()
                        .filter(p1.or(p2).or(p3).or(p4)).collect(Collectors.toList()));
            }
            try {
                reinitializeVBox(getUserSearchFormat(), usersSearchList);
               // messagesSearchList.setAll(getMessagesList());
                initializeVBox(getUserSearchFormat(),messagesSearchList);
                System.out.println(messagesSearchList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private List<UserDto> getUsersList() {
        List<UserDto> users = StreamSupport.stream(service.findAllUsers().spliterator(), false)
                .map(n -> new UserDto(n.getFirstName(), n.getLastName(), n.getEmail()))
                .collect(Collectors.toList());
        if (users.size() == 0)
            throw new ServiceException("No user with that name!");
        return users;
    }

    private List<MessageDto> getMessagesList(){
        List<MessageDto> messages = StreamSupport.stream(service.findMessages(service.getIdFromEmail(currentUser)).spliterator(),false)
                .map(n->new MessageDto(n.getFrom(),n.getTo(),n.getMessage(),n.getData()))
                .collect(Collectors.toList());
        if(messages.size() == 0)
            throw  new ServiceException("No messages found!");
        return messages;
    }
    private java.net.URL getUserSearchFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/MessageSearchView.fxml");
    }

    private AnchorPane getSearchMessagesFormatView(MessageDto message, URL formatURL) throws IOException {
        FXMLLoader searchMessageViewLoader = new FXMLLoader();
        searchMessageViewLoader.setLocation(formatURL);
        AnchorPane searchMessageView = new AnchorPane();
        searchMessageView = searchMessageViewLoader.load();
        AbstractController messageSearchController = searchMessageViewLoader.getController();
        messageSearchController.setMessageController(message, currentUser, service);
        return searchMessageView;
    }

    private AnchorPane getSearchUsersFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractController messageSearchController = searchUserViewLoader.getController();
        messageSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
