package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
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
import java.util.List;

public class MessagesController {
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


    public void setService(Service servicee,String userEmail) {
        this.service = servicee;
        //System.out.println(service.communitiesNumber()+"a");
        this.currentUser=userEmail;
        messagesList.setAll(getMessagesList());

    }

    private List<MessageDto> getMessagesList() {
        List<MessageDto> messages = service.findMessages(service.getIdFromEmail(currentUser));
       // if (messages.size() == 0)
            //throw new ServiceException("No messages found!");
        return messages;
    }


    @FXML
    private void initialize() {
        columnFrom.setCellValueFactory(new PropertyValueFactory<MessageDto, String>("From"));
        columnTo.setCellValueFactory(new PropertyValueFactory<MessageDto, String>("To"));
        columnMessage.setCellValueFactory(new PropertyValueFactory<MessageDto, String>("Message"));
        columnDate.setCellValueFactory(new PropertyValueFactory<MessageDto, String>("Data"));

        messagesTable.setItems(messagesList);

    }

    @FXML
    protected void onGoBackClick(){
        showUserView(currentUser);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
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

        UserController userController = fxmlLoader.getController();
        userController.setService(this.service,user);

        Stage userStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        userStage.setScene(scene);
        userStage.show();
    }
}
