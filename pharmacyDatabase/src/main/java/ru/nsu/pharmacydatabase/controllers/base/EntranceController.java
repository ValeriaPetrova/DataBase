package ru.nsu.pharmacydatabase.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.utils.Connection;
import ru.nsu.pharmacydatabase.utils.DBInit;
import ru.nsu.pharmacydatabase.Main;

import java.io.IOException;
import java.sql.SQLException;

public class EntranceController {
    public final static String LOGIN_WINDOW_FXML = "/ru/nsu/pharmacydatabase/windows/entrance_window.fxml";
    private final Connection connection;
    private final DBInit dbInit;
    @FXML
    private TextArea loginText;
    @FXML
    private PasswordField passwordText;

    public EntranceController() {
        connection = Main.getConnection();
        dbInit = new DBInit(connection);
    }

    @FXML
    public void loginButtonTapped(ActionEvent event) throws IOException {
        try {
            connection.registerConnection();
        } catch (SQLException ex) {
            System.out.println("SQLException: error with connection to server");
            showAlert("error with connection to server", "");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: error with driver manager");
            showAlert("error with driver manager", "");
        }
        if (isNotEmpty()) {
            try {
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResourceAsStream("/ru/nsu/pharmacydatabase/windows/admin_main_window.fxml"));
                primaryStage.setScene(new Scene(root));
            } catch (ExceptionInInitializerError ex) {
                System.out.println("ExceptionInInitializerError: session is already exist");
                showAlert("session is already exist", "");
            }
        }
    }

    private boolean isNotEmpty() {
        return loginText.getText().length() != 0 && passwordText.getText().length() != 0;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
