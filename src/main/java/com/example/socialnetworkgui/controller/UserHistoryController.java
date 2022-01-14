package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class UserHistoryController extends AbstractFriendsController{
    protected ObservableList<UserDto> friendsList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> messagesList = FXCollections.observableArrayList();
    protected ObservableList<MessageDto> conversationList = FXCollections.observableArrayList();
    protected List<UserDto> exportList=new ArrayList<>();
    protected List<ReplyMessage> exportMessagesList = new ArrayList<>();
    protected List<MessageDto> exportAllMessagesList = new ArrayList<>();

    @FXML
    VBox usersVBox;
    @FXML
    TextField textUser;
    @FXML
    DatePicker dateTo;
    @FXML
    DatePicker dateFrom;
    @FXML
    Button btnExport;

    private List<MessageDto> getMessagesList(){
        try{
            LocalDate date_from = dateFrom.getValue();
            LocalDate date_to = dateTo.getValue();

            List<MessageDto> messages = service.findMessages(service.getIdFromEmail(currentUser));
            if (messages.size() == 0)
                throw new ServiceException("No messages found!");
            if((date_from ==null) && (date_to==null)) {
                btnExport.setDisable(false);
                return messages;
            }
            else{
                List<MessageDto> messages_from_date = new ArrayList<>();
                if(date_from==null){
                    for(MessageDto msgs: messages){
                        LocalDateTime lcl_date_2 = msgs.getData();
                        LocalDate lcl_date=lcl_date_2.toLocalDate();
                        if (lcl_date.isBefore(date_to)){
                            messages_from_date.add(msgs);
                        }
                    }
                }
                else {
                    if (date_to==null) {
                        for (MessageDto msgs : messages) {
                            LocalDateTime lcl_date_2 = msgs.getData();
                            LocalDate lcl_date=lcl_date_2.toLocalDate();
                            if (lcl_date.isAfter(date_from)){
                                messages_from_date.add(msgs);
                            }
                        }
                    }
                    else {
                        for (MessageDto msgs : messages) {
                            LocalDateTime lcl_date_2 = msgs.getData();
                            LocalDate lcl_date=lcl_date_2.toLocalDate();
                            if (lcl_date.isBefore(date_to) && lcl_date.isAfter(date_from)){
                                messages_from_date.add(msgs);
                            }
                        }
                    }
                }
                btnExport.setDisable(false);
                return messages_from_date;
            }
        }catch (Exception ex){

        }
        return null;
    }

    private List<ReplyMessage> getConversationList(){
        try{
            LocalDate date_from = dateFrom.getValue();
            LocalDate date_to = dateTo.getValue();
            String user = textUser.getText();
            if(user == null){
                return null;
            }
            List<ReplyMessage> conversation = service.showConversation(currentUser,user);
            if (conversation.size() == 0)
                throw new ServiceException("No messages found!");
            if((date_from ==null) && (date_to==null)) {
                btnExport.setDisable(false);
                return conversation;
            }
            else{
                List<ReplyMessage> conversation_from_date = new ArrayList<>();
                if(date_from==null){
                    for(ReplyMessage conv: conversation){
                        LocalDateTime lcl_date_2 = conv.getData();
                        LocalDate lcl_date=lcl_date_2.toLocalDate();
                        if (lcl_date.isBefore(date_to)){
                            conversation_from_date.add(conv);
                        }
                    }
                }
                else {
                    if (date_to==null) {
                        for (ReplyMessage conv : conversation) {
                            LocalDateTime lcl_date_2 = conv.getData();
                            LocalDate lcl_date=lcl_date_2.toLocalDate();
                            if (lcl_date.isAfter(date_from)){
                                conversation_from_date.add(conv);
                            }
                        }
                    }
                    else {
                        for (ReplyMessage conv : conversation) {
                            LocalDateTime lcl_date_2 = conv.getData();
                            LocalDate lcl_date=lcl_date_2.toLocalDate();
                            if (lcl_date.isBefore(date_to) && lcl_date.isAfter(date_from)){
                                conversation_from_date.add(conv);
                            }
                        }
                    }
                }
                btnExport.setDisable(false);
                return conversation_from_date;
            }
        }catch (Exception ex){

        }
        return null;
    }

    private List<UserDto> getFriendsList() {
        try {
            LocalDate date_from = dateFrom.getValue();
            LocalDate date_to = dateTo.getValue();
            List<UserDto> friends = service.findFriends(service.getIdFromEmail(currentUser));
            if (friends.size() == 0)
                throw new ServiceException("No friends found!");
            if((date_from ==null) && (date_to==null)) {
                btnExport.setDisable(false);
                return friends;
            }
            else{
                List<UserDto> newFriends = new ArrayList<>();
                if(date_from==null){
                    for(UserDto friend: friends){
                        String email = friend.getEmail();
                        Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email),service.getIdFromEmail(currentUser));
                        LocalDate lcl_date=timestamp.toLocalDateTime().toLocalDate();
                        if (lcl_date.isBefore(date_to)){
                            newFriends.add(friend);
                        }
                    }
                }
                else {
                    if (date_to==null) {
                        for (UserDto friend : friends) {
                            String email = friend.getEmail();
                            Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email), service.getIdFromEmail(currentUser));
                            LocalDate lcl_date = timestamp.toLocalDateTime().toLocalDate();
                            if (lcl_date.isAfter(date_from)) {
                                newFriends.add(friend);
                            }
                        }
                    }
                    else {
                        for (UserDto friend : friends) {
                            String email = friend.getEmail();
                            Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email), service.getIdFromEmail(currentUser));
                            LocalDate lcl_date = timestamp.toLocalDateTime().toLocalDate();
                            if (lcl_date.isAfter(date_from) && lcl_date.isBefore(date_to)) {
                                newFriends.add(friend);
                            }
                        }
                    }
                }
                btnExport.setDisable(false);
                return newFriends;
            }
        }catch (Exception ex){

        }
        return null;
    }

    private AnchorPane getSearchFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractFriendsController userSearchController = searchUserViewLoader.getController();
        userSearchController.setUserController( user,currentUser, service);
        return searchUserView;
    }

    private AnchorPane getMessagesFormatView(MessageDto message, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractMessagesController userSearchController = searchUserViewLoader.getController();
        userSearchController.setMessageController( message,currentUser, service);
        return searchUserView;
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<UserDto> usersList) throws IOException {
        usersVBox.getChildren().clear();
        for (UserDto user : usersList) {
            usersVBox.getChildren().add(getSearchFormatView(user, formatURL));
        }
    }
    @FXML
    private void reinitializeVBox(URL formatURL,ObservableList<MessageDto> messagesList) throws IOException {

        usersVBox.getChildren().clear();
        for (MessageDto message : messagesList) {
            usersVBox.getChildren().add(getMessagesFormatView(message, formatURL));
        }
    }

    private java.net.URL getUserFriendFormat() {
        //super.setUserController(workingUser,currentUser,service);
        //AbstractController abstractController  = new UserFriendController();
        //abstractController.setAbstractController(currentUser,service);
        return getClass().getResource("/com/example/socialnetworkgui/views/UserFriendView.fxml");
    }

    private java.net.URL getMessageFormat(){
        return getClass().getResource("/com/example/socialnetworkgui/views/SentMessageView.fxml");
    }

    public void onExport(ActionEvent actionEvent) throws IOException {

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(imageFilter);
        File file = chooser.showSaveDialog(btnExport.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());


            List<String> message = new ArrayList<>();
            //String text ="";
            if(exportList.size()>0 || exportAllMessagesList.size()>0) {
                //System.out.println(exportList);
                if(exportList.size()>0) {
                    for (UserDto us : exportList) {
                        message.add(us.getFirstName() + " " + us.getLastName());
                        //text = text+" "+us.getFirstName()+" "+us.getLastName();
                    }
                }
                if(exportAllMessagesList.size()>0){
                    for(MessageDto msg: exportAllMessagesList){
                        List<Long> group = msg.getGroup();
                        String group_members="";
                        for(Long id: group){
                            group_members = group_members+" "+service.getEmailFromId(id);
                        }
                        group_members = group_members+" Last Message:";
                        message.add(group_members+" "+msg.getMessage());
                    }
                }
            }
            else
                if(exportMessagesList.size()>0){
                    for (ReplyMessage replyMessage : exportMessagesList) {
                        String from  = service.getEmailFromId(replyMessage.getFrom());
                        String to="";
                        for(Long id: replyMessage.getTo()){
                            to = to+" "+service.getEmailFromId(id);
                        }
                        to=to+" message:";
                        message.add(from+" "+to+" "+replyMessage.getMessage());
                        //text = text+" "+us.getFirstName()+" "+us.getLastName();
                    }
                }

            System.out.println(message);

            PDDocument doc = new PDDocument();
            try {
                PDPage page = new PDPage();
                doc.addPage(page);

                PDFont font = PDType1Font.HELVETICA_BOLD;

                PDPageContentStream contents = new PDPageContentStream(doc, page);
                contents.beginText();
                contents.setFont(font, 10);
                contents.newLineAtOffset(50, 700);
                //contents.showText(message.toString());
                for(var el:message){
                    contents.showText(el.toString());
                    contents.newLineAtOffset(0,-20);
                    System.out.println(el);
                }
                contents.endText();
                contents.close();

                doc.save(file.getAbsolutePath());
            }
            finally {
                doc.close();
            }

        }
    }

    public void onConversation(ActionEvent actionEvent){
        try {
            exportMessagesList.clear();
            conversationList.clear();
            exportAllMessagesList.clear();
            exportList.clear();
            exportMessagesList.addAll(getConversationList());
            List<MessageDto> conversation = new ArrayList<>();
            for(ReplyMessage replyMessage:exportMessagesList){
                MessageDto messageDto = new MessageDto(replyMessage.getFrom(), replyMessage.getTo(),replyMessage.getMessage(),replyMessage.getData());
                conversation.add(messageDto);
            }
            conversationList.setAll(conversation);
            reinitializeVBox(getMessageFormat(),conversationList);
        }catch (Exception ex){

        }
    }

    public void onMessages(ActionEvent actionEvent){
        try{
            //exportList.clear();
            messagesList.clear();
            exportMessagesList.clear();
            exportAllMessagesList.clear();
            messagesList.setAll(getMessagesList());
            exportAllMessagesList.addAll(messagesList);
            reinitializeVBox(getMessageFormat(),messagesList);
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

    public void onFriends(ActionEvent actionEvent) {
        try {
            exportMessagesList.clear();
            exportList.clear();
            friendsList.clear();
            friendsList.setAll(getFriendsList());
            exportList.addAll(friendsList);
            System.out.println(exportList);
            initializeVBox(getUserFriendFormat(), friendsList);
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

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}