package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RegisterController extends AbstractFriendsController {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label errorLabel;


    public void setRegisterController(Service service) {
        super.setUserController(null, null, service);
    }

    @FXML
    public StartupController onMouseClickedBack() {
        Stage stage = (Stage) firstNameLabel.getScene().getWindow();
        stage.close();
        FXMLLoader startupLoader = new FXMLLoader();
        startupLoader.setLocation(getClass().getResource("/com/example/socialnetworkgui/views/startup-view.fxml"));
        AnchorPane root = null;
        try {
            root = (AnchorPane) startupLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));

        StartupController startupController = startupLoader.getController();
        startupController.setService(service);
        primaryStage.setResizable(false);
        primaryStage.show();
        return startupController;
    }

    public void onRegisterClicked(ActionEvent actionEvent) {
        try {
            User user = this.service.addUser(firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getText());
            if (null == currentUser) {
                StartupController controller=onMouseClickedBack();
                controller.setAppName("Account created successfully! Log in!");
            } else {
                errorLabel.setText("Fail to register user, email is already being used!");
            }
        } catch (ValidatorException ve) {
            if (ve.getMessage().contains("Incorrect first name!\n")) {
                firstNameLabel.setTextFill(Paint.valueOf("red"));
                firstNameLabel.setText("Invalid First Name!");
            }
            if (ve.getMessage().contains("Incorrect last name!\n")) {
                lastNameLabel.setTextFill(Paint.valueOf("red"));
                lastNameLabel.setText("Invalid Last Name!");
            }
            if (ve.getMessage().contains("Invalid email!\n")) {
                emailLabel.setTextFill(Paint.valueOf("red"));
                emailLabel.setText("Invalid Email!");
            }
            if (ve.getMessage().contains("Invalid password!\n")) {
                passwordLabel.setTextFill(Paint.valueOf("red"));
                passwordLabel.setText("Invalid Password!");
            }
        } catch (RepositoryException re) {
            errorLabel.setText("Email already linked to an account!");
        }
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

    public void onMouseClickedFirstName(MouseEvent mouseEvent) {
        firstNameLabel.setTextFill(Paint.valueOf("#B2B2B2"));
        firstNameLabel.setText("First Name");
        lastNameLabel.setText("");
        passwordLabel.setText("");
        emailLabel.setText("");
    }

    public void onMouseClickedLastName(MouseEvent mouseEvent) {
        lastNameLabel.setTextFill(Paint.valueOf("#B2B2B2"));
        firstNameLabel.setText("");
        lastNameLabel.setText("Last Name");
        passwordLabel.setText("");
        emailLabel.setText("");
    }

    public void onMouseClickedEmail(MouseEvent mouseEvent) {
        emailLabel.setTextFill(Paint.valueOf("#B2B2B2"));
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        passwordLabel.setText("");
        emailLabel.setText("Email");
    }
    public void onMouseClickedPassword(MouseEvent mouseEvent) {
        passwordLabel.setTextFill(Paint.valueOf("#B2B2B2"));
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        passwordLabel.setText("Password");
        emailLabel.setText("");
    }

    public void onMouseClickedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) passwordLabel.getScene().getWindow();
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }

    public void onMouseDraggedPage(MouseEvent mouseEvent) {
        Stage stage = (Stage) passwordLabel.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }
}
