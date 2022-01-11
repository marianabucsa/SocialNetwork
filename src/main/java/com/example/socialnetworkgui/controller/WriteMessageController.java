package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.EventType;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteMessageController extends UserMessageController{
    String workingUser;
    MessageDto message;

    @FXML
    private Button btnSend;
    @FXML
    private TextArea textArea;
    @FXML
    private Label lblErrors;

    @FXML
    public void onSendClick(){
        String text = textArea.getText();
        if(text.equals("")){
            lblErrors.setTextFill(Paint.valueOf("red"));
            lblErrors.setText("Cannot send empty message!");
            return;
        }
        List<Long> all_members = workingMessage.getGroup();
        List<String> alls = new ArrayList<>();
        for(Long member: all_members){
            String user = service.getEmailFromId(member);
            if(!user.equals(currentUser)){
                alls.add(user);
            }
        }
        service.sendMessage(currentUser,alls,text);
        service.notifyObservers(new ServiceEvent(EventType.SENT_MESSAGE,workingMessage));
    }

   /* @Override
    public void setMessageController(MessageDto message, String currentUser, Service service) throws IOException {
        super.setMessageController(message,currentUser, service);
        workingUser = service.getEmailFromId(message.getFrom());
        this.message = message;
    }*/



}
