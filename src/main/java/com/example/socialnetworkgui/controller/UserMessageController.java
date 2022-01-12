package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.*;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserMessageController extends AbstractController {
    protected ObservableList<UserDto> usersSearchList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> messagesSearchList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> conversationList = FXCollections.observableArrayList();

    @FXML
    private TextField textSearchByName;
    @FXML
    private VBox usersVBox;// = new VBox();
    @FXML
    private ScrollPane userScrollPane;

    @FXML
    private Label errorUserSearchLabel;
    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;



    @FXML
    private void onShowMessages() throws IOException {
        messagesSearchList.setAll(getAllGroupsforUser(currentUser));
        initializeVBox(getUserSearchFormat(),messagesSearchList);

    }

    private boolean verifGroups(List<MessageDto> g1, List<MessageDto> g2) {
        //TODO
        return false;
    }

    private List<MessageDto> getAllGroupsforUser(String currentUser) {
        boolean exists = false;
        List<MessageDto> allMessages = service.findMessages(service.getIdFromEmail(currentUser));
        List<MessageDto> groups = new ArrayList<>();
        for (MessageDto msg : allMessages) {
            if (groups.isEmpty()) {
                msg.addMessage(msg);
                groups.add(msg);
            } else {
                List<Long> group = msg.getGroup();
                for (MessageDto messageDto : groups) {
                    if (messageDto.getGroup().containsAll(group) && group.containsAll(messageDto.getGroup())) {
                        messageDto.addMessage(msg);
                        exists = true;
                    }
                }
                if (!exists) {
                    msg.addMessage(msg);
                    groups.add(msg);
                }
                exists = false;
            }
        }
//        System.out.println("Grups:"+groups);
//        System.out.println("---");
//        for (MessageDto msg: groups){
//            System.out.println("Mess:"+msg.getConversation());
//        }

        return groups;

    }


    @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
        super.setUserController(null, currentUser, service);

    }

    @Override
    public void setMessageController(MessageDto message, String currentUser, Service service) throws IOException {
        super.setMessageController(null, currentUser, service);
       // this.workingMessage = message;

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
        usersVBox.getChildren().clear();
        for (MessageDto message : messagesList) {
           // usersVBox.getChildren().add(getSearchMessagesFormatView(message, formatURL));
            usersVBox.getChildren().add(getConversationFormatView(message,formatURL));
        }
    }

    @FXML
    private void addToVBox(MessageDto messageDto, String type) throws IOException {
        if(type.equals("received")) {
            usersVBox.getChildren().add(getConversationFormatView(messageDto, receivedMessageFormat()));
        }
        else
        {
            if(type.equals("sent"))
                usersVBox.getChildren().add(getConversationFormatView(messageDto, sentMessageFormat()));

        }
    }

    public void onSearchClick(ActionEvent actionEvent) {
        if (Objects.equals(textSearchByName.getText(), "")) {
            usersSearchList.clear();
            messagesSearchList.clear();
        } else {
            List<String> names = List.of(textSearchByName.getText().split(" "));
            for (String name : names) {
                Predicate<UserDto> p1 = n -> n.getFirstName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p2 = n -> n.getLastName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<UserDto> p3 = n -> n.getFirstName().startsWith(name.toLowerCase());
                Predicate<UserDto> p4 = n -> n.getLastName().startsWith(name.toLowerCase());
                usersSearchList.setAll(getUsersList().stream()
                        .filter(p1.or(p2).or(p3).or(p4)).collect(Collectors.toList()));
            }
            for (String name : names) {
                Predicate<MessageDto> p1 = n -> service.findOneUser(n.getFrom()).getFirstName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<MessageDto> p2 = n -> service.findOneUser(n.getFrom()).getLastName().startsWith(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
                Predicate<MessageDto> p3 = n -> service.findOneUser(n.getFrom()).getFirstName().startsWith(name.toLowerCase());
                Predicate<MessageDto> p4 = n -> service.findOneUser(n.getFrom()).getLastName().startsWith(name.toLowerCase());
                messagesSearchList.setAll(getMessagesList().stream()
                        .filter(p1.or(p2).or(p3).or(p4)).collect(Collectors.toList()));
            }
            try {
                reinitializeVBox(getUserSearchFormat(), usersSearchList);

                // initializeVBox(getUserSearchFormat(),messagesSearchList);
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

    private List<MessageDto> getMessagesList() {
        List<MessageDto> messages = StreamSupport.stream(service.findMessages(service.getIdFromEmail(currentUser)).spliterator(), false)
                .map(n -> new MessageDto(n.getFrom(), n.getTo(), n.getMessage(), n.getData()))
                .collect(Collectors.toList());
        if (messages.size() == 0)
            throw new ServiceException("No messages found!");
        return messages;
    }

    private java.net.URL getUserSearchFormat() throws IOException {
        super.setMessageController(workingMessage,currentUser,service);
        super.setUserController(workingUser,currentUser,service);
        return getClass().getResource("/com/example/socialnetworkgui/views/MessageSearchView.fxml");
    }

    /*private AnchorPane getSearchMessagesFormatView(MessageDto message, URL formatURL) throws IOException {
        FXMLLoader searchMessageViewLoader = new FXMLLoader();
        searchMessageViewLoader.setLocation(formatURL);
        AnchorPane searchMessageView = new AnchorPane();
        searchMessageView = searchMessageViewLoader.load();
        AbstractController messageSearchController = searchMessageViewLoader.getController();
        messageSearchController.setMessageController(message, currentUser, service);
        return searchMessageView;
    }*/

    private AnchorPane getSearchUsersFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractController messageSearchController = searchUserViewLoader.getController();
        messageSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    public java.net.URL sentMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/SentMessageView.fxml");
    }

    public java.net.URL receivedMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/ReceivedMessageView.fxml");
    }

    public java.net.URL writeMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/WriteMessageView.fxml");
    }

    public AnchorPane getConversationFormatView(MessageDto messageDto, URL formatURL) throws IOException {
        FXMLLoader conversationViewLoader = new FXMLLoader();
        conversationViewLoader.setLocation(formatURL);
        AnchorPane conversationView = new AnchorPane();
        conversationView = conversationViewLoader.load();
        AbstractController conversationController = conversationViewLoader.getController();
        conversationController.setMessageController(messageDto, currentUser, service);
        return conversationView;
    }

    @FXML
    public void createConversationScene(MessageDto msg) throws IOException {
        try {
            System.out.println("ooo");
            if(usersVBox.equals(null)) System.out.println("ofof");
            this.usersVBox.getChildren().clear();
            workingMessage = msg;
            List<MessageDto> conversation = new ArrayList<>();
            conversation.addAll(workingMessage.getConversation());
            System.out.println(conversation);
            //conversationList.setAll(workingMessage.getConversation());
            for(MessageDto messageDto: conversation){
                Long id = messageDto.getFrom();
                if (id.equals(service.getIdFromEmail(currentUser))){
                    addToVBox(messageDto,"sent");
                }
                else{
                    addToVBox(messageDto,"received");
                }
            }
            usersVBox.getChildren().add(getConversationFormatView(null,writeMessageFormat()));

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


    @FXML
    private void onSendMessageClick() throws IOException {
        //super.setMessageController(workingMessage,currentUser,service);
       // super.setUserController(workingUser,currentUser,service);
        //workingUser = user.getEmail();
        //usersVBox.
        userFirstLastName.setText(workingUser.getFirstName() + " " + workingUser.getLastName());
        usersVBox.getChildren().clear();

        //System.out.println("sdfgfdh"+ workingMessage);
        errorUserSearchLabel.setText(workingMessage.getConversation().size()+" messages");
        //super.setMessageController(message,currentUser,service);
        //service.notifyObservers(new ServiceEvent(EventType.SEND_MESSAGE, message));

       createConversationScene(workingMessage);

    }



    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {
        switch (serviceEvent.getEventType()) {
            case SEND_MESSAGE: {
                try {
                    //System.out.println("aaaaa");

                    //createConversationScene(workingMessage);

                    usersVBox.getChildren().clear();
                    workingMessage = (MessageDto) serviceEvent.getData();
                    conversationList.setAll(workingMessage.getConversation());
                    for(MessageDto messageDto: conversationList){
                        Long id = messageDto.getFrom();
                        if (id.equals(service.getIdFromEmail(currentUser))){
                            addToVBox(messageDto,"sent");
                        }
                        else{
                            addToVBox(messageDto,"received");
                        }
                    }
                    usersVBox.getChildren().add(getConversationFormatView(null,writeMessageFormat()));
                    //conversationList.clear();

                    break;
                } catch (ValidatorException ve) {
                    System.out.println("a");
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    System.out.println("b");
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    System.out.println("c");
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    System.out.println("d");
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    System.out.println("e");
                    usersVBox.getChildren().clear();
                    usersVBox.getChildren().add(new Text(e.getMessage()));
                }
            }
            case SENT_MESSAGE:{
                usersVBox.getChildren().clear();
            }
        }

    }
}
