package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;

public class UserMyEventsController extends AbstractEventsController {
    protected ObservableList<Event> myEventsList = FXCollections.observableArrayList();

    @FXML
    private VBox eventsVBox;

    @Override
    public void setAbstractController(String currentUser, Service service) {
        super.setAbstractController(currentUser, service);
        try {
            myEventsList.setAll(getMyEventsList());
            initializeVBox(getMyEventFormat(), myEventsList);
        } catch (ValidatorException ve) {
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(new Text(e.getMessage()));
        }
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<Event> eventsList) throws IOException {
        eventsVBox.getChildren().clear();
        for (Event event : eventsList) {
            eventsVBox.getChildren().add(getMyEventFormatView(event, formatURL));
        }
    }

    private AnchorPane getMyEventFormatView(Event event, URL formatURL) throws IOException {
        FXMLLoader eventsViewLoader = new FXMLLoader();
        eventsViewLoader.setLocation(formatURL);
        AnchorPane eventsView = new AnchorPane();
        eventsView = eventsViewLoader.load();
        AbstractEventsController eventsController = eventsViewLoader.getController();
        eventsController.setAbstractEventController(currentUser, service, event);
        return eventsView;
    }

    private List<Event> getMyEventsList() {
        List<Event> events = service.findUserEvents(service.getIdFromEmail(currentUser));
        if (events == null)
            throw new ServiceException("No events found!");
        return events;
    }

    private java.net.URL getMyEventFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserMyEventView.fxml");
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
