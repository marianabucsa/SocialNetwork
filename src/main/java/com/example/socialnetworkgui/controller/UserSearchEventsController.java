package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserSearchEventsController extends AbstractEventsController {

    @FXML
    private TextField searchTextField;

    protected ObservableList<Event> myEventsList = FXCollections.observableArrayList();

    @FXML
    private VBox eventsVBox;

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }

    public void onSearchAction(ActionEvent actionEvent) {
        try {
            if (Objects.equals(searchTextField.getText(), ""))
                myEventsList.clear();
            else {
                Predicate<Event> p1 = n -> n.getName().startsWith(searchTextField.getText().substring(0, 1).toUpperCase() + searchTextField.getText().substring(1).toLowerCase());
                Predicate<Event> p2 = n -> n.getName().startsWith(searchTextField.getText().toLowerCase());

                myEventsList.setAll(getSearchEventsList().stream()
                        .filter(p1.or(p2)).collect(Collectors.toList()));
            }
            if(myEventsList.size()==0)
                throw new ServiceException("No event found!");
            initializeVBox(getUserSearchFormat(), myEventsList);
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

    private URL getUserSearchFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserEventSearchView.fxml");
    }

    private List<Event> getSearchEventsList() {
        List<Event> events = StreamSupport.stream(service.findAllEvents().spliterator(), false)
                .collect(Collectors.toList());
        if (events.size() == 0)
            throw new ServiceException("No event with that name!");
        return events;
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<Event> evList) throws IOException {
        eventsVBox.getChildren().clear();
        for (Event event : evList) {
            eventsVBox.getChildren().add(getSearchFormatView(event, formatURL));
        }
    }

    private Node getSearchFormatView(Event event, URL formatURL) throws IOException {
        FXMLLoader searchEventViewLoader = new FXMLLoader();
        searchEventViewLoader.setLocation(formatURL);
        AnchorPane searchEventView = new AnchorPane();
        searchEventView = searchEventViewLoader.load();
        AbstractEventsController eventsSearchController = searchEventViewLoader.getController();
        eventsSearchController.setAbstractEventController(currentUser, service, event);
        return searchEventView;
    }


}

