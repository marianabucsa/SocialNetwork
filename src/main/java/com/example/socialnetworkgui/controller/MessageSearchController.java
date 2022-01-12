package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.MessageDto;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.EventType;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MessageSearchController extends UserMessageController{
    String workingUser;
    MessageDto message;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private Label userFirstLastName;

    @FXML
    private Label errorUserSearchLabel;

    public MessageSearchController() throws IOException {
    }

  /*  @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
       // Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        //circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setUserController(user,currentUser, service);
        workingUser = user.getEmail();
        userFirstLastName.setText(user.getFirstName() + " " + user.getLastName());
    }
    */
    @Override
    public void setMessageController(MessageDto message, String currentUser, Service service) throws IOException {
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        super.setMessageController(message,currentUser, service);
        workingUser = service.getEmailFromId(message.getFrom());
        this.message = message;
        ArrayList<String> to = new ArrayList<>();

        User current_user = service.findOneUser(service.getIdFromEmail(currentUser));
        String current_user_name = current_user.getFirstName()+" "+current_user.getLastName();

        for ( Long id: message.getTo()){
            User u = service.findOneUser(id);
            String name = u.getFirstName()+" "+u.getLastName();
            if(!name.equals(current_user_name)) {
                String name2 = u.getFirstName();

                to.add(name2);
            }
        }
        User current_us = service.findOneUser(message.getFrom());
        String name = current_us.getFirstName()+" "+current_us.getLastName();
        if(!name.equals(current_user_name)) {
            userFirstLastName.setText(current_us.getFirstName());
            for (String s:to) {
                userFirstLastName.setText(userFirstLastName.getText()+" "+s);
            }
        }
        else
        {
            userFirstLastName.setText(to.toString());
        }
    }

    @FXML
    private void onSendMessageClick() throws IOException {
        //super.usersVBox.getChildren().clear();
        errorUserSearchLabel.setText(message.getConversation().size()+" messages");
        //super.setMessageController(message,currentUser,service);
        service.notifyObservers(new ServiceEvent(EventType.SEND_MESSAGE, message));

        //System.out.println("A notificat:))");
        //super.createConversationScene(message);

    }



    /*private void setScene(URL viewURL) throws IOException {
        Parent sceneToAdd = getSceneToOpen(viewURL);
        Node sceneToRemove = container.getChildren().get(0);

        sceneToAdd.setTranslateX(container.getWidth());
        container.getChildren().add(sceneToAdd);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(sceneToAdd.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> {
            container.getChildren().remove(sceneToRemove);
        });
        timeline.play();
    }

    private Parent getSceneToOpen(URL formatURL) throws IOException {
        FXMLLoader sceneLoader = new FXMLLoader();
        sceneLoader.setLocation(formatURL);
        Parent sceneToOpen = sceneLoader.load();
        AbstractController abstractController = sceneLoader.getController();
        abstractController.setUserController(null,currentUser,service);
        return sceneToOpen;
    }*/
}
