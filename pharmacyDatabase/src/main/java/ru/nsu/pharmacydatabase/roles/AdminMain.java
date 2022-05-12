package ru.nsu.pharmacydatabase.roles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.controllerrs.MainController;

import java.io.IOException;

public class AdminMain {
    @FXML
    public Button button;

    @FXML
    public void getAll() {
        showSelectWindow(MainController.FXML);
    }

    private void showSelectWindow(String name) {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(getClass().getResourceAsStream(name));
            primaryStage.setScene(new Scene(root));
        } catch (IOException ignored) {
        }
        primaryStage.show();
    }

}
