package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class ComposeMessageController {
    String currentUser;
    Service service;

    @FXML
    protected Label lblTo;
    @FXML
    protected Label lblCC;
    @FXML
    protected Label lblSubject;
    @FXML
    protected TextArea textArea;
    @FXML
    protected Button btnSend;
    @FXML
    protected TextField textTo;
    @FXML
    protected TextField textCC;
    @FXML
    protected TextField textSubject;
    @FXML
    protected Label lblErrors;

    public void setService(Service service, String user) {
        this.service=service;
        this.currentUser=user;
    }

    @FXML
    protected void onCancelClick(){
        showMessagesView();
        Stage stage = (Stage) lblTo.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onSendClick(){
        try {
            String to = textTo.getText();
            if (to.isEmpty()){
                lblErrors.setAlignment(Pos.CENTER);
                lblErrors.setTextFill(Paint.valueOf("red"));
                lblErrors.setText("No receiver found!");
            }
            else {
                ArrayList<String> alls = new ArrayList<>();
                alls.add(to);
                String cc = textCC.getText();
                List<String> ccList = Arrays.asList(cc.split(";"));
                if (!ccList.isEmpty() && !cc.isEmpty()) {
                    for(String c:ccList) {
                        alls.add(c);
                    }
                }
                String subject = textSubject.getText();
                String message = textArea.getText();
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

    private void showMessagesView(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/messages-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            MessagesController messagesController = loader.getController();
            messagesController.setService(service,currentUser);

            Stage messageStage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            messageStage.setScene(scene);
            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}