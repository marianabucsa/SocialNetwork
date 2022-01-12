package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class UserEventsController extends AbstractEventsController {

    @FXML
    private StackPane container;

    public void setEventsController(Event event, String userEmail, Service service) {
        super.setAbstractEventController(userEmail, service,event);
    }

    private Parent getSceneToOpen(URL formatURL) throws IOException {
        FXMLLoader sceneLoader = new FXMLLoader();
        sceneLoader.setLocation(formatURL);
        Parent sceneToOpen = sceneLoader.load();
        AbstractEventsController abstractController = sceneLoader.getController();
        abstractController.setAbstractController( currentUser, service);
        return sceneToOpen;
    }

    private void setScene(URL viewURL) throws IOException {
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

    public void onMyEventsClicked(ActionEvent actionEvent) throws IOException {
        setScene(getClass().getResource("/com/example/socialnetworkgui/views/UserMyEventsView.fxml"));
    }

    public void onCreateEventClicked(ActionEvent actionEvent) throws IOException {
        setScene(getClass().getResource("/com/example/socialnetworkgui/views/UserCreateEventView.fxml"));
    }

    public void onFriendsClicked(ActionEvent actionEvent) {
    }

    public void onSearchEventClicked(ActionEvent actionEvent) {
    }

    public void onSignedUpEventsClicked(ActionEvent actionEvent) {
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

}
