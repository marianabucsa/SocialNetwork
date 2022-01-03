package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.ReplyMessage;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class ReplyMessageController {
    String currentUser;
    Service service;
    MessageDto message;

    @FXML
    protected Label lblTo;
    @FXML
    protected TextArea textArea;
    @FXML
    protected Button btnSend;
    @FXML
    protected TextField textTo;
    @FXML
    protected Label lblErrors;
    @FXML
    protected CheckBox checkBoxReplyAll;

    public void setService(Service service, String user, MessageDto message) {
        this.service=service;
        this.currentUser=user;
        this.message = message;
    }

    @FXML
    protected void onCancelClick(){
        showMessagesView();
        Stage stage = (Stage) lblTo.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onCheckReplyAllClick(){
        for (ReplyMessage replyMessage : service.getToReplyForUser(currentUser)) {
            System.out.print("Message: "+ replyMessage.getId()+"\n" +
                    "From: " + service.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            lblErrors.setText("Message: "+ replyMessage.getId()+"\n" +
                    "From: " + service.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            for(Long id: replyMessage.getTo())
                System.out.print(service.getEmailFromId(id)+" ");
            System.out.println("\nData: " + replyMessage.getData() + "\n\n"
                    + replyMessage.getMessage() + "\n");
        }

    }

    @FXML
    protected void onSendClick(){
        try {
            //List<String> alls = null;
            String to = textTo.getText();
            if (to.isEmpty()){
                lblErrors.setAlignment(Pos.CENTER);
                lblErrors.setTextFill(Paint.valueOf("red"));
                lblErrors.setText("No receiver found!");
            }
            else {
                ArrayList<String> alls = new ArrayList<>();
                String to_users = textTo.getText();
                List<String> ccList = Arrays.asList(to_users.split(";"));
                //ccList.add(cc);
                if (!ccList.isEmpty() && !to_users.isEmpty()) {
                    //alls = ccList;
                    for(String c:ccList) {
                        //System.out.println(ccList);
                        //System.out.println(c);
                        alls.add(c);
                    }
                }
                /*alls.add(to);
                String cc = textCC.getText();
                List<String> ccList = Arrays.asList(cc.split(";"));
                //ccList.add(cc);
                if (!ccList.isEmpty() && !cc.isEmpty()) {
                    //alls = ccList;
                    for(String c:ccList) {
                        System.out.println(ccList);
                        System.out.println(c);
                        alls.add(c);
                    }
                }*/
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
            loader.setLocation(getClass().getResource("views/messages-view.fxml"));

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
