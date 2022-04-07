package ru.nsu.databaseproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    ConnectController connectController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ru/nsu/databaseproject/connect.fxml"));
        fxmlLoader.load(getClass().getResource("/connect.fxml").openStream());
        connectController = fxmlLoader.getController();
        stage.setTitle("Connection");
        stage.setScene(new Scene((connectController.connectPane)));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}