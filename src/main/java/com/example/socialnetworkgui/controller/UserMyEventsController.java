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
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;

public class UserMyEventsController extends AbstractEventsController {
    protected ObservableList<Event> myEventsList = FXCollections.observableArrayList();

    @FXML
    private ScrollPane eventsSPane;
    @FXML
    private VBox eventsVBox;

    Pagination pagination;

    @Override
    public void setAbstractController(String currentUser, Service service) {
        super.setAbstractController(currentUser, service);
        try {
            myEventsList.setAll(getMyEventsList());
            initializeVBox(getMyEventFormat(), myEventsList);
        } catch (ValidatorException ve) {
            eventsVBox.getChildren().clear();
            Text text= new Text(ve.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(ve.getMessage());
            eventsVBox.getChildren().add(text);
            eventsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (ServiceException se) {
            eventsVBox.getChildren().clear();
            Text text= new Text(se.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(se.getMessage());
            eventsVBox.getChildren().add(text);
            eventsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (RepositoryException re) {
            eventsVBox.getChildren().clear();
            Text text= new Text(re.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(re.getMessage());
            eventsVBox.getChildren().add(text);
            eventsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (InputMismatchException ime) {
            eventsVBox.getChildren().clear();
            Text text= new Text(ime.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(ime.getMessage());
            eventsVBox.getChildren().add(text);
            eventsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (IOException e) {
            eventsVBox.getChildren().clear();
            Text text= new Text(e.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(e.getMessage());
            eventsVBox.getChildren().add(text);
            eventsVBox.setAlignment(Pos.valueOf("CENTER"));
        }
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<Event> eventsList) throws IOException {
        eventsVBox.getChildren().clear();
        if (eventsList.size()==0)
            throw new ServiceException("No events found!");
        pagination = new Pagination(eventsList.size(), 1);
        pagination.setPageFactory((Integer pageIndex) -> {
            if (pageIndex >= eventsList.size()) {
                return null;
            } else {
                try {
                    return createPage(pageIndex,formatURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        eventsVBox.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex,URL formatURL) throws IOException {
        VBox vb= new VBox(5);
        Set<Event> events = service.getEventsUserOnPage(pageIndex,1, service.getIdFromEmail(currentUser));
        for (Event event : events) {
            vb.getChildren().add(getMyEventFormatView(event, formatURL));
        }
        return vb;
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
        if (events.size() == 0)
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
