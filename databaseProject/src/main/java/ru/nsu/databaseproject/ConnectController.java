package ru.nsu.databaseproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.io.IOException;

public class ConnectController {
    @FXML
    public AnchorPane connectPane;
    @FXML
    private TextArea infoArea;
    @FXML
    private Button myServerButton;
    @FXML
    private TextField ipField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;

    private void openMenu(String username) {
    }

    @FXML
    public void connectToCourseServer() throws SQLException, IOException {

    }

}
