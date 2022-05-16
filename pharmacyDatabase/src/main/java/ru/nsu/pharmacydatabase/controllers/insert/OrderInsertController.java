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

public class OrderInsertController implements InsertController, Initializable {
    private DBInit dbInit;
    private ChangeListener listener;
    private ObservableStringValue name = new SimpleStringProperty("");
    private InsertMode insertMode = InsertMode.insert;
    private String item;
    @FXML
    private Button insertButton;
    @FXML
    private TextField medicamentIdField;
    @FXML
    private TextField isReadyField;
    @FXML
    private TextField isReceivedField;
    @FXML
    private TextField startDateField;

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
        String medicamentId = DBInit.getSubstring(" MEDICAM_ID=", "MEDICAM_ID=", item);
        String isReady = DBInit.getSubstring(" IS_READY=", "IS_READY=", item);
        String isReceived = DBInit.getSubstring(" IS_RECEIVED=", "IS_RECEIVED=", item);
        String startDate = DBInit.getSubstring(" START_DATE=", "START_DATE=", item);
        medicamentIdField.setText(medicamentId);
        isReadyField.setText(isReady);
        isReceivedField.setText(isReceived);
        startDateField.setText(startDate);
    }

    public void insertButtonTapped(ActionEvent actionEvent) {
        String medicamentId = medicamentIdField.getText();
        int medicamId = Integer.parseInt(medicamentId);
        String isReady = isReadyField.getText();
        String isReceived = isReceivedField.getText();
        String startDate = startDateField.getText();
        if (medicamentIdField.getText().isEmpty() || isReceivedField.getText().isEmpty() || isReadyField.getText().isEmpty() || startDateField.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else if (!isReady.equals("YES") && !isReady.equals("NO")) {
            showAlert("Wrong format!", "Enter YES or NO on the line 'готов'");
        } else if (!isReceived.equals("YES") && !isReceived.equals("NO")) {
            showAlert("Wrong format!", "Enter YES or NO on the line 'получен'");
        } else {
            if (insertMode == InsertMode.insert) {
                dbInit.insertOrder(medicamId, isReady, isReceived, startDate);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updateOrder(id, medicamId, isReady, isReceived, startDate);
            }
            listener.changed(name, "", name);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        }

    }
}
