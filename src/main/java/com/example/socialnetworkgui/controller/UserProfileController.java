package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class UserProfileController extends AbstractFriendsController {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Circle circleProfilePicture;

    @FXML
    private StackPane container;

    @FXML
    private Label userProfileName;

    @Override
    public void setUserController(UserDto user, String userEmail, Service service) {
        super.setUserController(user, userEmail, service);
        Image profilePicture = new Image("/com/example/socialnetworkgui/pictures/defaultPicture.png");
        circleProfilePicture.setFill(new ImagePattern(profilePicture));
        User userName = service.findOneUser(service.getIdFromEmail(userEmail));
        userProfileName.setText(userName.getFirstName() + " " + userName.getLastName());
    }

    public void onLogOutClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) circleProfilePicture.getScene().getWindow();
        stage.close();

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/startup-view.fxml"));
        AnchorPane loginPane = null;
        try {
            loginPane = (AnchorPane) loginLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartupController loginController = loginLoader.getController();
        loginController.setService(this.service);

        Stage loginStage = new Stage();
        Scene scene = new Scene(loginPane);
        loginStage.setScene(scene);
        loginStage.initStyle(StageStyle.TRANSPARENT);
        loginStage.show();
    }

    public void onMessagesClick(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/messages-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            MessagesController messagesController = loader.getController();
            messagesController.setService(service, currentUser);

            Stage messageStage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            messageStage.setScene(scene);
            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onNotificationClick(ActionEvent actionEvent) {
    }

    public void onEventsClick(ActionEvent actionEvent) throws IOException {
        setScene(getClass().getResource("/com/example/socialnetworkgui/views/UserEventsView.fxml"));
    }

    public void onReportsClick(ActionEvent actionEvent) {
    }


    public void onCommunityClick(ActionEvent actionEvent) throws IOException {
        setScene(getClass().getResource("/com/example/socialnetworkgui/views/UserUsersView.fxml"));
    }

    public void onEditProfileClicked(MouseEvent mouseEvent) throws IOException {
        setScene(getClass().getResource("/com/example/socialnetworkgui/views/EditProfileView.fxml"));
    }

    private Parent getSceneToOpen(URL formatURL) throws IOException {
        FXMLLoader sceneLoader = new FXMLLoader();
        sceneLoader.setLocation(formatURL);
        Parent sceneToOpen = sceneLoader.load();
        AbstractController abstractController = sceneLoader.getController();
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

    @Override
    public void update(ServiceEvent serviceEvent) {

    }

    public void onMouseClickedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) circleProfilePicture.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }

    public void onMouseDraggedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) circleProfilePicture.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }

    public void onDeleteAccountClicked(ActionEvent actionEvent) {
        try {
            service.deleteUser(currentUser);
            Stage stage = (Stage) circleProfilePicture.getScene().getWindow();
            stage.close();

            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/startup-view.fxml"));
            AnchorPane loginPane = null;
            try {
                loginPane = (AnchorPane) loginLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StartupController loginController = loginLoader.getController();
            loginController.setService(this.service);

            Stage loginStage = new Stage();
            Scene scene = new Scene(loginPane);
            loginStage.setScene(scene);
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.show();
        } catch (ServiceException se) {
            se.printStackTrace();
        } catch (ValidatorException ve) {
            ve.printStackTrace();
        } catch (RepositoryException re) {
            re.printStackTrace();
        }
    }
}
