package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class UserModifyMyEventController extends AbstractEventsController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private Label errorLabel;

    public void onModifyEventAction(ActionEvent actionEvent) {
        try {
            if (startPicker.getValue() == null || endPicker.getValue() == null) {
                if (startPicker.getValue() == null) {
                    startPicker.setValue(LocalDate.from(workingEvent.getStartDate()));
                }
                if (endPicker.getValue() == null) {
                    endPicker.setValue(LocalDate.from(workingEvent.getEndDate()));
                }
            }
            if (!startTimeTextField.getText().matches("[0-9][0-9]:[0-9][0-9]") || !endTimeTextField.getText().matches("[0-9][0-9]:[0-9][0-9]")) {
                errorLabel.setAlignment(Pos.CENTER);
                errorLabel.setTextFill(Paint.valueOf("red"));
                errorLabel.setText("Time format incorrect!");
            } else {
                List<String> startTime = Arrays.asList(startTimeTextField.getText().split(":"));
                List<String> endTime = Arrays.asList(endTimeTextField.getText().split(":"));
                if (Integer.parseInt(startTime.get(0)) < 24 && Integer.parseInt(startTime.get(0)) >= 0 && Integer.parseInt(startTime.get(1)) < 60 && Integer.parseInt(startTime.get(1)) >= 0 && Integer.parseInt(endTime.get(0)) < 24 && Integer.parseInt(endTime.get(0)) >= 0 && Integer.parseInt(endTime.get(1)) < 60 && Integer.parseInt(endTime.get(1)) >= 0) {
                    service.updateEvent(workingEvent.getId(), nameTextField.getText(), startPicker.getValue().atTime(Integer.parseInt(startTime.get(0)), Integer.parseInt(startTime.get(1))), endPicker.getValue().atTime(Integer.parseInt(endTime.get(0)), Integer.parseInt(endTime.get(1))), descriptionTextField.getText(), locationTextField.getText(), service.getIdFromEmail(currentUser), null);
                    errorLabel.setAlignment(Pos.CENTER);
                    errorLabel.setTextFill(Paint.valueOf("green"));
                    errorLabel.setText("Event modified!");
                } else {
                    errorLabel.setAlignment(Pos.CENTER);
                    errorLabel.setTextFill(Paint.valueOf("red"));
                    errorLabel.setText("Time format incorrect!");
                }
            }
        } catch (ValidatorException ve) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(ve.getMessage());
        } catch (ServiceException se) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(se.getMessage());
        } catch (RepositoryException re) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(re.getMessage());
        } catch (InputMismatchException ime) {
            errorLabel.setAlignment(Pos.CENTER);
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText(ime.getMessage());
        }
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

    @Override
    public void setAbstractEventController(String currentUser, Service service, Event event) {
        super.setAbstractEventController(currentUser, service, event);
        nameTextField.setText(workingEvent.getName());
        locationTextField.setText(workingEvent.getLocation());
        descriptionTextField.setText(workingEvent.getDescription());
        startPicker.setPromptText(workingEvent.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        endPicker.setPromptText(workingEvent.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        LocalDateTime sTime = LocalDateTime.parse(workingEvent.getStartDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime eTime = LocalDateTime.parse(workingEvent.getEndDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
        startTimeTextField.setText(sTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        endTimeTextField.setText(eTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public void onBackAction(ActionEvent actionEvent) {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }
}
