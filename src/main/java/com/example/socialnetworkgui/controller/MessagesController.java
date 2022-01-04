package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessagesController extends AbstractController {
    String currentUser;
    Service service;
    ObservableList<MessageDto> messagesList = FXCollections.observableArrayList();

    @FXML
    private Button btnBack;
    @FXML
    private TableColumn<MessageDto, String> columnMessage;
    @FXML
    private TableColumn<MessageDto, String> columnFrom;
    @FXML
    private TableColumn<MessageDto, String> columnTo;
    @FXML
    private TableColumn<MessageDto, String> columnDate;

    @FXML
    private TableView<MessageDto> messagesTable;


    public void setMessagesController(Service service, String userEmail) {
        super.setUserController(null,userEmail,service);
        messagesList.setAll(getMessagesList());

    }

    /**
     *
     * @return List of messagedto grouped by users
     */
    private List<MessageDto> getOthers(){
        List<MessageDto> allMessages = getMessagesList();

        //messageGroup<Destinatari,NrMesaje>
        HashMap<List<Long>,Integer> messageGroup = null;

        Long current_user_id = service.getIdFromEmail(currentUser);

        for (MessageDto m: allMessages) {
            Long from = m.getFrom();
            List<Long> to = m.getTo();
            if(!Objects.equals(from, current_user_id)){
                //cautam destinatarul in lista de destinatari
                //daca nu gasim adaugam destinatarul
                //daca gasim incrementam nr de mesaje
                List<Long> destinatar=null;
                destinatar.add(from);
               if (messageGroup.keySet()==null){
                    messageGroup.put(destinatar,1);
               }
               else {
                   for (List<Long> destinatari : messageGroup.keySet()) {
                        if(destinatari.equals(destinatar)){

                        }
                   }
               }
            }
        }
        return allMessages;
    }

    private List<MessageDto> getMessagesList() {
        List<MessageDto> messages = service.findMessages(service.getIdFromEmail(currentUser));
       // if (messages.size() == 0)
            //throw new ServiceException("No messages found!");
        return messages;
    }


    @FXML
    private void initialize() {
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("From"));
        columnTo.setCellValueFactory(new PropertyValueFactory<>("To"));
        columnMessage.setCellValueFactory(new PropertyValueFactory<>("Message"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("Data"));

        messagesTable.setItems(messagesList);
    }

    @FXML
    protected void onGoBackClick(){
        showUserView(currentUser);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onComposeClick(){
        showComposeMessageView(currentUser);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.hide();
    }

    public void showComposeMessageView(String user){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/composeMessage-view.fxml"));
        AnchorPane root= null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComposeMessageController composeMessageController = fxmlLoader.getController();
        composeMessageController.setService(this.service,user);

        Stage composeMessageStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        composeMessageStage.setScene(scene);
        composeMessageStage.show();
    }


    public void showUserView(String user)  {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UserView.fxml"));
        AnchorPane root= null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AbstractController userMessagesController = fxmlLoader.getController();
        userMessagesController.setUserController(null, user,this.service);

        Stage userStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        userStage.setScene(scene);
        userStage.show();
    }

    @Override
    public void update(ServiceEvent serviceEvent) {

    }
}
