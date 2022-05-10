package ru.nsu.pharmacydatabase;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class Main extends Application {
    private static final Connection connection = new Connection();
    private static final DBInit dbinit = new DBInit(connection);

    public static void main (String[] args) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        //launch(args);
        connection.registerDefaultConnection();
        dbinit.init();
        connection.close();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //connection.registerDefaultConnection();
    }

    @Override
    public void stop() throws Exception {
//        connection.close();
//        super.stop();
    }
}
