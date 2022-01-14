package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class UserMessageControllerV2 extends AbstractMessagesController{

    protected ObservableList<MessageDto> messagesSearchList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> conversationList = FXCollections.observableArrayList();
    @FXML
    VBox vBoxMessages;
    @FXML
    VBox vBoxConversation;
    @FXML
    Button btnSend;
    @FXML
    TextArea textToSend;
    @FXML
    Label lblName;
    @FXML
    Label lblMessages;
    @FXML
    Circle userPicture;
    @FXML
    ScrollPane scrMessages;
    @FXML
    ScrollPane scrConversation;
    @FXML
    Button btnCompose;
    @FXML
    Label lblTo;
    @FXML
    TextField textTo;
    @FXML
    TextArea composeText;
    @FXML
    Button btnSendCompose;
    @FXML
    Pane paneCompose;
    @FXML
    Label lblErrors;

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<MessageDto> messagesList) throws IOException {
        vBoxMessages.getChildren().clear();
        for (MessageDto message : messagesList) {
            vBoxMessages.getChildren().add(getConversationFormatView(message,formatURL));
        }
    }

    private java.net.URL getUserSearchFormat() throws IOException {
        super.setMessageController(workingMessage,currentUser,service);
        return getClass().getResource("/com/example/socialnetworkgui/views/MessageSearchView.fxml");
    }

    private AnchorPane getSearchUsersFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractFriendsController messageSearchController = searchUserViewLoader.getController();
        messageSearchController.setUserController( user,currentUser, service);
        return searchUserView;
    }

    public AnchorPane getConversationFormatView(MessageDto messageDto, URL formatURL) throws IOException {
        FXMLLoader conversationViewLoader = new FXMLLoader();
        conversationViewLoader.setLocation(formatURL);
        AnchorPane conversationView = new AnchorPane();
        conversationView = conversationViewLoader.load();
        AbstractMessagesController conversationController = conversationViewLoader.getController();
        conversationController.setMessageController(messageDto, currentUser, service);
        return conversationView;
    }

    private void setConversationBox(){
        scrConversation.setVisible(true);
        vBoxConversation.setVisible(true);
        btnSend.setVisible(true);
        textToSend.setVisible(true);
    }

    private  void hideComposeElements(){
        paneCompose.setVisible(false);
        lblTo.setVisible(false);
        textTo.setVisible(false);
        composeText.setVisible(false);
        btnSendCompose.setVisible(false);
        lblErrors.setVisible(false);
    }

    private void showComposeElements(){
        vBoxConversation.getChildren().clear();
        scrConversation.setVisible(false);
        vBoxConversation.setVisible(false);
        btnSend.setVisible(false);
        textToSend.setVisible(false);
        scrConversation.setVisible(true);
        vBoxConversation.setVisible(true);
        paneCompose.setVisible(true);
        lblTo.setVisible(true);
        textTo.setVisible(true);
        composeText.setVisible(true);
        btnSendCompose.setVisible(true);
        lblErrors.setVisible(true);
    }

    @Override
    public void setAbstractController(String currentUser, Service service) {
        super.setAbstractController(currentUser, service);
        scrConversation.setVisible(false);
        vBoxConversation.setVisible(false);
        btnSend.setVisible(false);
        textToSend.setVisible(false);

        hideComposeElements();

        lblName.setText(currentUser);
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        userPicture.setFill(new ImagePattern(profilePicture));
        messagesSearchList.setAll(getAllGroupsforUser(currentUser));
        try {
            initializeVBox(getUserSearchFormat(), messagesSearchList);
        }catch (IOException ie){

        }
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

        return groups;

    }

    public java.net.URL sentMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/SentMessageView.fxml");
    }

    public java.net.URL receivedMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/ReceivedMessageView.fxml");
    }

    @FXML
    private void addToVBox(MessageDto messageDto, String type) throws IOException {
        if(type.equals("received")) {
            vBoxConversation.getChildren().add(getConversationFormatView(messageDto, receivedMessageFormat()));
        }
        else
        {
            if(type.equals("sent"))
                vBoxConversation.getChildren().add(getConversationFormatView(messageDto, sentMessageFormat()));

        }
    }



    @FXML
    private void onComposeMessage(){
        showComposeElements();

    }

    @FXML
    private void onSendCompose(){
        try {

            ArrayList<String> alls = new ArrayList<>();
            String cc = textTo.getText();
            List<String> ccList = Arrays.asList(cc.split(";"));
            if (!ccList.isEmpty() && !cc.isEmpty()) {
                for(String c:ccList) {
                    alls.add(c);
                }
            }
            String message = composeText.getText();
            if(alls.size()==0 || message.isEmpty()){
                lblErrors.setAlignment(Pos.CENTER);
                lblErrors.setTextFill(Paint.valueOf("red"));
                lblErrors.setText("Invalid message!");
            }
            else {
                service.sendMessage(currentUser, alls, message);
                lblErrors.setAlignment(Pos.CENTER);
                lblErrors.setTextFill(Paint.valueOf("green"));
                lblErrors.setText("Message sent!");
            }

        }catch (ServiceException se){
            lblErrors.setAlignment(Pos.CENTER);
            lblErrors.setTextFill(Paint.valueOf("red"));
             lblErrors.setText(se.getMessage());
        }catch (ValidatorException ve){
            lblErrors.setAlignment(Pos.CENTER);
            lblErrors.setTextFill(Paint.valueOf("red"));
             lblErrors.setText(ve.getMessage());
        }catch (RepositoryException re){
             lblErrors.setAlignment(Pos.CENTER);
             lblErrors.setTextFill(Paint.valueOf("red"));
             lblErrors.setText(re.getMessage());
        }catch (InputMismatchException ie){
             lblErrors.setAlignment(Pos.CENTER);
              lblErrors.setTextFill(Paint.valueOf("red"));
             lblErrors.setText(ie.getMessage());
        }

    }

    @FXML
    private void onSendMessage()  {
        String text = textToSend.getText();
        textToSend.setText("");
        Long from = service.getIdFromEmail(currentUser);
        List<Long> to = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();
        to = workingMessage.getTo();
        List<String> to_emails = new ArrayList<>();
        for(Long id: to){
            String email = service.getEmailFromId(id);
            to_emails.add(email);
        }
        MessageDto messageDto = new MessageDto(from,to,text,date);
        service.sendMessage(currentUser,to_emails,text);
        try {
            addToVBox(messageDto, "sent");
        }catch (IOException io){

        }
    }


    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {
        switch (serviceEvent.getEventType()) {
            case SEND_MESSAGE: {
                try {
                    hideComposeElements();
                    System.out.println("Ai apasat butonul send");
                    setConversationBox();

                    vBoxConversation.getChildren().clear();
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

                    break;
                } catch (ValidatorException ve) {
                    System.out.println("a");
                    vBoxConversation.getChildren().clear();
                    vBoxConversation.getChildren().add(new Text(ve.getMessage()));
                } catch (ServiceException se) {
                    System.out.println("b");
                    vBoxConversation.getChildren().clear();
                    vBoxConversation.getChildren().add(new Text(se.getMessage()));
                } catch (RepositoryException re) {
                    System.out.println("c");
                    vBoxConversation.getChildren().clear();
                    vBoxConversation.getChildren().add(new Text(re.getMessage()));
                } catch (InputMismatchException ime) {
                    System.out.println("d");
                    vBoxConversation.getChildren().clear();
                    vBoxConversation.getChildren().add(new Text(ime.getMessage()));
                } catch (IOException e) {
                    System.out.println("e");
                    vBoxConversation.getChildren().clear();
                    vBoxConversation.getChildren().add(new Text(e.getMessage()));
                }
            }
        }

    }
}
