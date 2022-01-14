package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.paging.Page;
import com.example.socialnetworkgui.repository.paging.Pageable;
import com.example.socialnetworkgui.repository.paging.PageableInterface;
import com.example.socialnetworkgui.repository.paging.Paginator;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserShowParticipantsController extends AbstractEventsController {
    protected ObservableList<UserDto> participantsList = FXCollections.observableArrayList();
    @FXML
    private VBox participantsVBox;
    private Pagination pagination;

    @Override
    public void setAbstractEventController(String currentUser, Service service, Event event) {
        super.setAbstractEventController(currentUser, service,event);
        try {
            participantsList.setAll(getParticipantsList());
            initializeVBox(getParticipantFormat(), participantsList);
        }  catch (ValidatorException ve) {
            participantsVBox.getChildren().clear();
            Text text= new Text(ve.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(ve.getMessage());
            participantsVBox.getChildren().add(text);
            participantsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (ServiceException se) {
            participantsVBox.getChildren().clear();
            Text text= new Text(se.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(se.getMessage());
            participantsVBox.getChildren().add(text);
            participantsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (RepositoryException re) {
            participantsVBox.getChildren().clear();
            Text text= new Text(re.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(re.getMessage());
            participantsVBox.getChildren().add(text);
            participantsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (InputMismatchException ime) {
            participantsVBox.getChildren().clear();
            Text text= new Text(ime.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(ime.getMessage());
            participantsVBox.getChildren().add(text);
            participantsVBox.setAlignment(Pos.valueOf("CENTER"));
        } catch (IOException e) {
            participantsVBox.getChildren().clear();
            Text text= new Text(e.getMessage());
            text.setFill(Color.valueOf("#B2B2B2"));
            text.setText(e.getMessage());
            participantsVBox.getChildren().add(text);
            participantsVBox.setAlignment(Pos.valueOf("CENTER"));
        }
    }

    private AnchorPane getParticipantFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractFriendsController userSearchController = searchUserViewLoader.getController();
        userSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    private URL getParticipantFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserSearchView.fxml");
    }

    private List<UserDto> getParticipantsList() {
        List<Long> part = workingEvent.getParticipants();
        if (part != null) {
            List<UserDto> users = new ArrayList<>();
            for (Long id : part) {
                User us = service.findOneUser(id);
                UserDto user = new UserDto(us.getFirstName(), us.getLastName(), us.getEmail());
                users.add(user);
            }
            return users;
        } else {
            throw new ServiceException("No participants yet!");
        }
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<UserDto> participantsList) throws IOException {
        participantsVBox.getChildren().clear();
        if (participantsList.size()==0)
            throw new ServiceException("No participants yet!");
        int pageCount;
        float nr=(float)participantsList.size()/3;
        if(nr>1)
            pageCount=participantsList.size()/3+1;
        else
            pageCount=1;
        pagination = new Pagination(pageCount, 1);
        pagination.setPageFactory((Integer pageIndex) -> {
            if (pageIndex >= participantsList.size()) {
                return null;
            } else {
                try {
                    return createPage(pageIndex,formatURL,participantsList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        participantsVBox.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex,URL formatURL,ObservableList<UserDto> participantsList) throws IOException {
        VBox vb= new VBox(0);
        PageableInterface pageable = new Pageable(pageIndex, 3);
        Paginator<UserDto> paginator = new Paginator<UserDto>(pageable, participantsList);
        Page<UserDto> participantsPage = paginator.paginate();
        Set<UserDto> participants = participantsPage.getContent().collect(Collectors.toSet());
        for (UserDto event : participants) {
            vb.getChildren().add(getParticipantFormatView(event, formatURL));
        }
        vb.setAlignment(Pos.valueOf("CENTER"));
        return vb;
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) participantsVBox.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
