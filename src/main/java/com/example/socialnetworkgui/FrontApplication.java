package com.example.socialnetworkgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontApplication extends Application {
    @Override
    public void start(Stage frontStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FrontApplication.class.getResource("FrontView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 290, 214);
        frontStage.setScene(scene);
        frontStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
