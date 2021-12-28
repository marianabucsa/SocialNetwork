package com.example.socialnetworkgui;

import com.example.socialnetworkgui.StartupController;
import com.example.socialnetworkgui.controller.MessageAlert;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class RegisterController {

    @FXML
    private TextField textFirstName;
    @FXML
    private TextField textLastName;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;
    @FXML
    private Label ErrorLabelText;

    private Service service;
    Stage dialogStage;
    User user;

    public void setService(Service service,  Stage stage, User u) {
        this.service = service;
        this.dialogStage=stage;
        this.user=u;
        if (null != u) {
            setFields(u);
        }
    }
    private void setFields(User us)
    {
        textFirstName.setText(us.getFirstName());
        textLastName.setText(us.getLastName());
        textEmail.setText(us.getEmail());
        textPassword.setText(us.getPassword());
    }

    @FXML
    public void handleRegister(){
        String fName=textFirstName.getText();
        String lName=textLastName.getText();
        String email=textEmail.getText();
        String password=textPassword.getText();
        User us=new User(fName,lName,email);
        us.setPassword(password);
        if (null == this.user)
            saveUser(us);
        //showLoginView();
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
        showLoginView();
    }

    private void saveUser(User us)
    {
        // TODO
        try{
            User r= this.service.addUser(us.getFirstName(),us.getLastName(), us.getEmail(), us.getPassword());
            if(r==null) {
                //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Register","User has been added!");
                showErrorLabelText(Alert.AlertType.CONFIRMATION,"User has been added!");
                // ErrorLabelText.setAlignment(Pos.CENTER);
                // ErrorLabelText.setTextFill(Paint.valueOf("green"));
                // ErrorLabelText.setText("User has been added!");
            }
            else {
                //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Register", "Fail to register user, email is already being used!");
                showErrorLabelText(Alert.AlertType.ERROR, "Fail to register user, email is already being used!");
                // ErrorLabelText.setAlignment(Pos.CENTER);
                // ErrorLabelText.setTextFill(Paint.valueOf("red"));
                // ErrorLabelText.setText( "Fail to register user, email is already being used!");
            }

        }catch (ValidatorException ve){
            // MessageAlert.showErrorMessage(null,ve.getMessage());
            showErrorLabelText(Alert.AlertType.ERROR, ve.getMessage());
            // ErrorLabelText.setAlignment(Pos.CENTER);
            // ErrorLabelText.setTextFill(Paint.valueOf("red"));
            // ErrorLabelText.setText(ve.getMessage());
        }catch (RepositoryException re){
            //MessageAlert.showErrorMessage(null, re.getMessage()+"Email already exists!");
            showErrorLabelText(Alert.AlertType.ERROR, re.getMessage());
            // ErrorLabelText.setAlignment(Pos.CENTER);
            // ErrorLabelText.setTextFill(Paint.valueOf("red"));
            // ErrorLabelText.setText(re.getMessage());
        }
        //dialogStage.close();

    }

    /**
     * Show if the current task has been succesfuly executed
     * @param type (AlertType), ERROR if the task failed  CONFIRMATION if the task succeeded
     * @param message the message to be shown
     */
    public void showErrorLabelText(Alert.AlertType type, String message){
        ErrorLabelText.setAlignment(Pos.CENTER);
        if(type.equals(Alert.AlertType.ERROR)){
            ErrorLabelText.setTextFill(Paint.valueOf("red"));
            ErrorLabelText.setText(message);
        }
        if(type.equals(Alert.AlertType.CONFIRMATION)){
            ErrorLabelText.setTextFill(Paint.valueOf("green"));
            ErrorLabelText.setText(message);
        }


    }

    public void showLoginView(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("startup-view.fxml"));
        AnchorPane root= null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartupController startupController = fxmlLoader.getController();
        startupController.setService(service);

        Stage loginStage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        loginStage.setScene(scene);
        loginStage.show();
    }
}
