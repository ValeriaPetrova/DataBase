package ru.nsu.pharmacydatabase.controllers.insert;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestInsertController implements InsertController, Initializable {
    private DBInit dbInit;
    private ChangeListener listener;
    private ObservableStringValue name = new SimpleStringProperty("");
    private InsertMode insertMode = InsertMode.insert;
    private String item;
    @FXML
    private Button insertButton;
    @FXML
    private TextField providerIdField;

    @Override
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
    }

    public void setMode(InsertMode mode) {
        insertMode = mode;
    }

    public void setItem(String item) {
        this.item = item;
        insertButton.setText("Изменить");
        String providerId = DBInit.getSubstring(" PROVIDER_ID=", "PROVIDER_ID=", item);
        providerIdField.setText(providerId);
    }

    public void insertButtonTapped(ActionEvent actionEvent) {
        if (providerIdField.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else {
            int providerId = Integer.parseInt(providerIdField.getText());
            if (insertMode == InsertMode.insert) {
                dbInit.insertRequest(providerId);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updateRequest(id, providerId);
            }
            listener.changed(name, "", name);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        }

    }
}
