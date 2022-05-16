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

public class PatientInsertController implements InsertController, Initializable {
    private DBInit dbInit;
    private ChangeListener listener;
    private ObservableStringValue name = new SimpleStringProperty("");
    private InsertMode insertMode = InsertMode.insert;
    private String item;
    @FXML
    private Button insertButton;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField birthdateField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField addressField;
    @FXML
    private  TextField registrationDateField;

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
        String firstname = DBInit.getSubstring(" PATIENT_FIRSTNAME=", "PATIENT_FIRSTNAME=", item);
        String surname = DBInit.getSubstring(" PATIENT_SURNAME=", "PATIENT_SURNAME=", item);
        String birthdate = DBInit.getSubstring(" PATIENT_BIRTHDATE=", "PATIENT_BIRTHDATE=", item);
        String phoneNumber = DBInit.getSubstring(" PATIENT_PHONE_NUMBER=", "PATIENT_PHONE_NUMBER=", item);
        String address = DBInit.getSubstring(" PATIENT_ADDRESS=", "PATIENT_ADDRESS=", item);
        String registrationDate = DBInit.getSubstring(" REGISTRATION_DATE=", "REGISTRATION_DATE=", item);
        firstnameField.setText(firstname);
        surnameField.setText(surname);
        birthdateField.setText(birthdate);
        phoneNumberField.setText(phoneNumber);
        addressField.setText(address);
        registrationDateField.setText(registrationDate);
    }

    public void insertButtonTapped(ActionEvent actionEvent) {
        String firstname = firstnameField.getText();
        String surname = surnameField.getText();
        String birthdate = birthdateField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String registrationDate = registrationDateField.getText();
        if (firstnameField.getText().isEmpty() || surnameField.getText().isEmpty() || birthdateField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || addressField.getText().isEmpty() || registrationDateField.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else {
            if (insertMode == InsertMode.insert) {
                dbInit.insertPatient(firstname, surname, birthdate, phoneNumber, address, registrationDate);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updatePatient(id, firstname, surname, birthdate, phoneNumber, address, registrationDate);
            }
            listener.changed(name, "", name);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        }

    }
}
