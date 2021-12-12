package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.RegisterController;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.*;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartupController {
    Service service;

    public void setService(Service serv){
        service=serv;
    }

    @FXML
    private Label appName;

    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;

    @FXML
    protected void onLoginButtonClick(){
        String id = textEmail.getText();
        String password = textPassword.getText();
        try {
            User user = service.findOneUser(service.getIdFromEmail(id));
            String u_password = user.getPassword();

            //if user password is null -> not an encrypted password in DB
            if(u_password == null) {
                appName.setText("ERROR -- not an encrypted password!");
                return;
            }

            //Check if the passwords are the same
            if(u_password.equals(password)) {
                showUserView(user);
                Stage stage = (Stage) appName.getScene().getWindow();
                stage.close();
            }
            else
                appName.setText("Incorrect password!");
        } catch (ServiceException se){
            appName.setText(se.getMessage());
        }catch (ValidatorException ve){
            appName.setText(ve.getMessage());
        }catch (RepositoryException re) {
            appName.setText("User not found!");
        }
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent ev){
        textEmail.clear();
        textPassword.clear();
        appName.setText("");
        showRegisterDialog(null);
    }

    public void showUserView(User user)  {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UserView.fxml"));
        AnchorPane root= null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserController userController = fxmlLoader.getController();
        userController.setService(this.service,user.getEmail());

        Stage userStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        userStage.setScene(scene);
        userStage.show();
    }

    public void showRegisterDialog(User user){
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/register-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Register");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            RegisterController registerController = loader.getController();
            registerController.setService(service, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
