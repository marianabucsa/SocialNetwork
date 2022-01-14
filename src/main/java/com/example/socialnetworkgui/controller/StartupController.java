package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartupController extends AbstractController {

    public void setService(Service serv) {
        super.setAbstractController(null, serv);
    }

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Label appName;

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;

    @FXML
    private Button btnLogin;

    @FXML
    protected void onLoginButtonClick() {
        String id = textEmail.getText();
        String password = textPassword.getText();
        try {
            User user = service.findOneUser(service.getIdFromEmail(id));
            String u_password = user.getPassword();

            //if user password is null -> not an encrypted password in DB
            if (u_password == null) {
                appName.setText("ERROR -- not an encrypted password!");
                return;
            }

            //Check if the passwords are the same
            if (u_password.equals(password)) {
                showUserView(user);
                Stage stage = (Stage) appName.getScene().getWindow();
                stage.close();
            } else {
                appName.setText("Incorrect password!");
                passwordLabel.setText("");
                emailLabel.setText("");
            }
        } catch (ServiceException se) {
            appName.setText(se.getMessage());
            passwordLabel.setText("");
            emailLabel.setText("");
        } catch (ValidatorException ve) {
            appName.setText(ve.getMessage());
            passwordLabel.setText("");
            emailLabel.setText("");
        } catch (RepositoryException re) {
            appName.setText("User not found!");
            passwordLabel.setText("");
            emailLabel.setText("");
        }
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent ev) {
        textEmail.clear();
        textPassword.clear();
        appName.setText("");
        emailLabel.setText("");
        passwordLabel.setText("");
        showRegisterDialog(null);
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }

    public void showUserView(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/UserProfile.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AbstractFriendsController userProfileController = fxmlLoader.getController();
        userProfileController.setUserController(null,user.getEmail(), this.service);

        Stage userStage = new Stage();
        Scene scene = new Scene(root);
        userStage.initStyle(StageStyle.TRANSPARENT);
        userStage.setScene(scene);
        userStage.show();
    }

    public void setAppName(String string){
        appName.setTextFill(Paint.valueOf("green"));
        appName.setText(string);
    }
    public void showRegisterDialog(User user) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/register-view.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialogStage = new Stage();

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        RegisterController registerController = loader.getController();
        registerController.setRegisterController(service);
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.show();

    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

    public void onMouseClickedEmail(MouseEvent mouseEvent) {
        emailLabel.setText("Email");
        passwordLabel.setText("");
        appName.setText("");
    }

    public void onMouseClickedPassword(MouseEvent mouseEvent) {
        passwordLabel.setText("Password");
        emailLabel.setText("");
        appName.setText("");
    }

    public void onMouseClickedClose(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }

    public void onMouseClickedMinimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setIconified(true);
    }

    public void onMouseClickedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }

    public void onMouseDraggedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }
}
