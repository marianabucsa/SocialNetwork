package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserEditProfileController extends AbstractFriendsController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox showPasswordCheckBox;

    @Override
    public void setUserController(UserDto user, String currentUser, Service service) {
        super.setUserController(user, currentUser, service);
        User userInfo = service.findOneUser(service.getIdFromEmail(currentUser));
        firstNameTextField.setText(userInfo.getFirstName());
        lastNameTextField.setText(userInfo.getLastName());
        emailTextField.setText(userInfo.getEmail());
        passwordTextField.setText(userInfo.getPassword());
    }

    public void onEditProfileClicked(ActionEvent actionEvent) {
        try {
            service.updateUser(service.getIdFromEmail(currentUser), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getText());
            currentUser = emailTextField.getText();
        } catch (
                ServiceException se) {
            errorLabel.setText(se.getMessage());
        } catch (
                ValidatorException ve) {
            errorLabel.setText(ve.getMessage());
        } catch (
                RepositoryException re) {
            errorLabel.setText("User not found!");

        }
    }

    public void onShowPasswordClicked(ActionEvent actionEvent) {
        User userInfo = service.findOneUser(service.getIdFromEmail(currentUser));
        String password = passwordTextField.getText();
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.clear();
            passwordTextField.setPromptText(password);
        } else
            passwordTextField.setText(userInfo.getPassword());
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
