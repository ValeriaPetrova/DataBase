package ru.nsu.pharmacydatabase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.controllers.base.EntranceController;
import ru.nsu.pharmacydatabase.utils.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class Main extends Application {
    private static final Connection connection = new Connection();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("PHARMACY");
        Locale.setDefault(new Locale("ru", "RU"));
        InputStream inputStream = getClass().getResourceAsStream(EntranceController.LOGIN_WINDOW_FXML);
        Parent root = new FXMLLoader().load(inputStream);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.close();
        super.stop();
    }

    public static Connection getConnection() {
        return connection;
    }
}