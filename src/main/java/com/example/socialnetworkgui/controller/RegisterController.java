package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class RegisterController extends AbstractController{

    @FXML
    private TextField textFirstName;
    @FXML
    private TextField textLastName;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;

    Stage dialogStage;
    User user;

    public void setService(Service service,  Stage stage, User u) {
        super.setUserController(null,u.getEmail(),service);
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
            saveMessage(us);
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    private void saveMessage(User us)
    {
        // TODO
        try{
            User r= this.service.addUser(us.getFirstName(),us.getLastName(), us.getEmail(), us.getPassword());
            if(r==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Register","User has been added!");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Register","Fail to register user, email is already being used!");

        }catch (ValidatorException ve){
            MessageAlert.showErrorMessage(null,ve.getMessage());
        }catch (RepositoryException re){
            MessageAlert.showErrorMessage(null, re.getMessage()+"Email already exists!");
        }
        dialogStage.close();

    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
