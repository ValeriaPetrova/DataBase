package ru.nsu.pharmacydatabase.controllers.insert;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
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
    private ChoiceBox choiceBoxMedicament;
    @FXML
    private ChoiceBox isReadyField;
    @FXML
    private ChoiceBox isReceivedField;
    @FXML
    private TextField startDateField;

    private ObservableList<String> itemsMedicament = FXCollections .<String>observableArrayList();
    private ObservableList<String> itemsIsReady = FXCollections .<String>observableArrayList();
    private ObservableList<String> itemsIsReceive = FXCollections .<String>observableArrayList();
    private Map<String, Integer> Medicament;

    @Override
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
        choiceBoxMedicament.setItems(itemsMedicament);
        isReadyField.setItems(itemsIsReady);
        isReceivedField.setItems(itemsIsReceive);
        try {
            ResultSet setMedicament = connection.executeQueryAndGetResult("select distinct title from medicament");
            Medicament = new HashMap<>();
            itemsMedicament.clear();

            itemsIsReady.clear();
            itemsIsReady.add("YES");
            itemsIsReady.add("NO");
            itemsIsReceive.clear();
            itemsIsReceive.add("YES");
            itemsIsReceive.add("NO");

            Medicament.put("all", 0);
            int id = 1;
            if (setMedicament != null) {
                while (setMedicament.next()) {
                    String name = setMedicament.getString(1);
                    Medicament.put(name, id);
                    itemsMedicament.add(name);
                    id += 1;
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMode(InsertMode mode) {
        insertMode = mode;
    }

    public void setItem(String item) {
        this.item = item;
        insertButton.setText("Изменить");
        String medicament = DBInit.getSubstring(" TITLE=", "TITLE=", item);
        String isReady = DBInit.getSubstring(" IS_READY=", "IS_READY=", item);
        String isReceived = DBInit.getSubstring(" IS_RECEIVED=", "IS_RECEIVED=", item);
        String startDate = DBInit.getSubstring(" START_DATE=", "START_DATE=", item);
        choiceBoxMedicament.setValue(medicament);
        isReadyField.setValue(isReady);
        isReceivedField.setValue(isReceived);
        startDateField.setText(startDate);
    }

    public void insertButtonTapped(ActionEvent actionEvent) {
        int medicamentId = Medicament.get(choiceBoxMedicament.getValue().toString());
        String isReady = isReadyField.getValue().toString();
        String isReceived = isReceivedField.getValue().toString();
        String startDate = startDateField.getText();
        if (choiceBoxMedicament.getSelectionModel().isEmpty() || isReceivedField.getSelectionModel().isEmpty() ||
                isReadyField.getSelectionModel().isEmpty() || startDateField.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } if (isReady.equals("NO") && isReceived.equals("YES")) {
            showAlert("Wrong data", "the order cannot be received if it is not yet ready");
        } else {
            if (insertMode == InsertMode.insert) {
                dbInit.insertOrder(medicamentId, isReady, isReceived, startDate);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updateOrder(id, medicamentId, isReady, isReceived, startDate);
            }
            listener.changed(name, "", name);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        }
    }

}
