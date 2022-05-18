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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PrescriptionInsertController implements InsertController, Initializable {
    private DBInit dbInit;
    private ChangeListener listener;
    private ObservableStringValue name = new SimpleStringProperty("");
    private InsertMode insertMode = InsertMode.insert;
    private String item;
    @FXML
    private Button insertButton;
    @FXML
    private TextField countField;
    @FXML
    private TextField directionForUseField;
    @FXML
    private TextField diagnosisField;
    @FXML
    private ChoiceBox doctorChoiceBox;
    @FXML
    private ChoiceBox patientChoiceBox;

    private ObservableList<String> itemsDoctor = FXCollections.<String>observableArrayList();
    private ObservableList<String> itemsPatient = FXCollections .<String>observableArrayList();
    private Map<String, Integer> Doctor;
    private Map<String, Integer> Patient;

    @Override
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
        doctorChoiceBox.setItems(itemsDoctor);
        patientChoiceBox.setItems(itemsPatient);
        try {
            ResultSet setDoctor = connection.executeQueryAndGetResult("select * from doctor");
            ResultSet setPatient = connection.executeQueryAndGetResult("select * from patient");
            Doctor = new HashMap<>();
            itemsDoctor.clear();
            itemsDoctor.add("new");
            Doctor.put("new", 0);
            Patient = new HashMap<>();
            itemsPatient.clear();

            if (setDoctor != null) {
                while (setDoctor.next()) {
                    String firstName = setDoctor.getString(2);
                    String surName = setDoctor.getString(3);
                    Integer id = setDoctor.getInt(1);
                    Doctor.put(firstName + " " + surName, id);
                    itemsDoctor.add(firstName + " " + surName);
                }
            }
            if (setPatient != null) {
                while (setPatient.next()) {
                    String firstName = setPatient.getString(2);
                    String surName = setPatient.getString(3);
                    Integer id = setPatient.getInt(1);
                    Patient.put(firstName + " " + surName, id);
                    itemsPatient.add(firstName + " " + surName);
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
        String count = DBInit.getSubstring(" NUMBER_OF_MEDICINES=", "NUMBER_OF_MEDICINES=", item);
        String dirForUse = DBInit.getSubstring(" DIRECTION_FOR_USE=", "DIRECTION_FOR_USE=", item);
        String diagnosis = DBInit.getSubstring(" DIAGNOSIS=", "DIAGNOSIS=", item);
        String doctorName = DBInit.getSubstring(" DOCTOR_NAME=", "DOCTOR_NAME=", item);
        String patientName = DBInit.getSubstring(" PATIENT_NAME=", "PATIENT_NAME=", item);
        System.out.println(item);

        countField.setText(count);
        directionForUseField.setText(dirForUse);
        diagnosisField.setText(diagnosis);
        doctorChoiceBox.setValue(doctorName);
        patientChoiceBox.setValue(patientName);
    }

    public void insertButtonTapped(ActionEvent actionEvent) {
        if (countField.getText().isEmpty() || diagnosisField.getText().isEmpty() || patientChoiceBox.getSelectionModel().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else if (doctorChoiceBox.getValue().equals("new")) {
            String count = countField.getText();
            int num = Integer.valueOf(count);
            String dirForUse = directionForUseField.getText();
            String diagnosis = diagnosisField.getText();
            String doctor = doctorChoiceBox.getValue().toString();
            int doctorId = Doctor.get(doctor);
            String patient = patientChoiceBox.getValue().toString();
            int patientId = Patient.get(patient);

            if (insertMode == InsertMode.insert) {
                dbInit.insertPrescription(num, dirForUse, diagnosis, doctorId, patientId);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updatePrescription(id, num, dirForUse, diagnosis, doctorId, patientId);
            }
            listener.changed(name, "", name);
            Stage stage1 = (Stage) insertButton.getScene().getWindow();
            stage1.close();
        } else {
            String count = countField.getText();
            int num = Integer.valueOf(count);
            String dirForUse = directionForUseField.getText();
            String diagnosis = diagnosisField.getText();
            String doctor = doctorChoiceBox.getValue().toString();
            int doctorId = Doctor.get(doctor);
            String patient = patientChoiceBox.getValue().toString();
            int patientId = Patient.get(patient);

            if (insertMode == InsertMode.insert) {
                dbInit.insertPrescription(num, dirForUse, diagnosis, doctorId, patientId);
            } else {
                int id = DBInit.getIdFrom(item);
                dbInit.updatePrescription(id, num, dirForUse, diagnosis, doctorId, patientId);
            }
            listener.changed(name, "", name);
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        }

    }
}
