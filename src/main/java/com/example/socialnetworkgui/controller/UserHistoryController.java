package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.service.ServiceException;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class UserHistoryController extends AbstractController{
    protected ObservableList<UserDto> friendsList = FXCollections.observableArrayList();
    protected List<UserDto> exportList=new ArrayList<>();

    @FXML
    VBox usersVBox;
    @FXML
    TextField textUser;
    @FXML
    DatePicker dateTo;
    @FXML
    DatePicker dateFrom;
    @FXML
    Button btnExport;

    private List<UserDto> getFriendsList() {
        try {
            LocalDate date_from = dateFrom.getValue();
            //exportList = new ArrayList<>();
            //System.out.println(from);
            LocalDate date_to = dateTo.getValue();
            List<UserDto> friends = service.findFriends(service.getIdFromEmail(currentUser));
            if (friends.size() == 0)
                throw new ServiceException("No friends found!");
            if((date_from ==null) && (date_to==null)) {
                btnExport.setDisable(false);
                exportList.clear();
                exportList.addAll(friends);
                return friends;
            }
            else{
                List<UserDto> newFriends = new ArrayList<>();
                if(date_from==null){
                    for(UserDto friend: friends){
                        String email = friend.getEmail();
                        Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email),service.getIdFromEmail(currentUser));
                        LocalDate lcl_date=timestamp.toLocalDateTime().toLocalDate();
                        if (lcl_date.isBefore(date_to)){
                            newFriends.add(friend);
                        }
                    }
                }
                else {
                    if (date_to==null) {
                        for (UserDto friend : friends) {
                            String email = friend.getEmail();
                            Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email), service.getIdFromEmail(currentUser));
                            LocalDate lcl_date = timestamp.toLocalDateTime().toLocalDate();
                            if (lcl_date.isAfter(date_from)) {
                                newFriends.add(friend);
                            }
                        }
                    }
                    else {
                        for (UserDto friend : friends) {
                            String email = friend.getEmail();
                            Timestamp timestamp = service.FriendshipDate(service.getIdFromEmail(email), service.getIdFromEmail(currentUser));
                            LocalDate lcl_date = timestamp.toLocalDateTime().toLocalDate();
                            if (lcl_date.isAfter(date_from) && lcl_date.isBefore(date_to)) {
                                newFriends.add(friend);
                            }
                        }
                    }
                }
                btnExport.setDisable(false);
                exportList.clear();
                exportList.addAll(newFriends);
                return newFriends;
            }
        }catch (Exception ex){

        }
        return null;
    }

    private AnchorPane getSearchFormatView(UserDto user, URL formatURL) throws IOException {
        FXMLLoader searchUserViewLoader = new FXMLLoader();
        searchUserViewLoader.setLocation(formatURL);
        AnchorPane searchUserView = new AnchorPane();
        searchUserView = searchUserViewLoader.load();
        AbstractController userSearchController = searchUserViewLoader.getController();
        userSearchController.setUserController(user, currentUser, service);
        return searchUserView;
    }

    @FXML
    private void initializeVBox(URL formatURL, ObservableList<UserDto> usersList) throws IOException {
        usersVBox.getChildren().clear();
        for (UserDto user : usersList) {
            usersVBox.getChildren().add(getSearchFormatView(user, formatURL));
        }
    }
    private java.net.URL getUserFriendFormat() {
        return getClass().getResource("/com/example/socialnetworkgui/views/UserFriendView.fxml");
    }

    public void onExport(ActionEvent actionEvent) throws IOException {
        /*PDDocument pdfdoc= new PDDocument();
        pdfdoc.addPage(new PDPage());
        //path where the PDF file will be store
        pdfdoc.save("C:\\Users\\Dragos\\Desktop\\Java PDF\\Sample.pdf");
        //prints the message if the PDF is created successfully
        System.out.println("PDF created");
        //closes the document
        pdfdoc.close();*/
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(imageFilter);
        File file = chooser.showSaveDialog(btnExport.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            PDDocument pdfdoc= new PDDocument();
            pdfdoc.addPage(new PDPage());
            //path where the PDF file will be store
           // pdfdoc.save("C:\\Users\\Dragos\\Desktop\\Java PDF\\Sample.pdf");


            PDPage firstPage=pdfdoc.getPage(0);
            PDFont pdfFont= PDType1Font.HELVETICA_BOLD;
            int fontSize = 14;
            //PDPageContentStream contentStream = new PDPageContentStream(pdfdoc, firstPage, PDPageContentStream.AppendMode.APPEND,true,true);
            //contentStream.setFont(pdfFont, fontSize);
           // contentStream.beginText();
            //contentStream.newLineAtOffset(200,685);
           // contentStream.showText("John");
            //contentStream.endText();


            pdfdoc.save(file.getAbsolutePath());
            //prints the message if the PDF is created successfully
            System.out.println("PDF created");
            //closes the document
            pdfdoc.close();

        }
    }

    public void onFriends(ActionEvent actionEvent) {
        try {
            friendsList.setAll(getFriendsList());
            initializeVBox(getUserFriendFormat(), friendsList);
        } catch (ValidatorException ve) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ve.getMessage()));
        } catch (ServiceException se) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(se.getMessage()));
        } catch (RepositoryException re) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(re.getMessage()));
        } catch (InputMismatchException ime) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(ime.getMessage()));
        } catch (IOException e) {
            usersVBox.getChildren().clear();
            usersVBox.getChildren().add(new Text(e.getMessage()));
        }

    }

    @Override
    public void update(ServiceEvent serviceEvent) throws IOException {

    }
}
