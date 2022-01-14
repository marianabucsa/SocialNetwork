package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.paging.Page;
import com.example.socialnetworkgui.repository.paging.Pageable;
import com.example.socialnetworkgui.repository.paging.PageableInterface;
import com.example.socialnetworkgui.repository.paging.Paginator;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserSearchEventsController extends AbstractEventsController {

    @FXML
    private TextField searchTextField;

    protected ObservableList<Event> myEventsList = FXCollections.observableArrayList();

    @FXML
    private VBox eventsVBox;

    private Pagination pagination;

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
                    return createPage(pageIndex,formatURL,eventsList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        eventsVBox.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex,URL formatURL,ObservableList<Event> eventsList) throws IOException {
        VBox vb= new VBox(5);
        PageableInterface pageable = new Pageable(pageIndex, 1);
        Paginator<Event> paginator = new Paginator<Event>(pageable, eventsList);
        Page<Event> eventPage = paginator.paginate();
        Set<Event> events = eventPage.getContent().collect(Collectors.toSet());
        for (Event event : events) {
            vb.getChildren().add(getSearchFormatView(event, formatURL));
        }
        return vb;
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

