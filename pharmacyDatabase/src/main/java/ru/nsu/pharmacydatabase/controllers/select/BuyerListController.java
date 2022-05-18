package ru.nsu.pharmacydatabase.controllers.select;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.net.URL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BuyerListController implements SelectController, Initializable {
    @FXML
    public Button listButton;
    @FXML
    public Button numButton;
    @FXML
    public TextField startDate;
    @FXML
    public TextField endDate;
    @FXML
    public ChoiceBox choiceBoxMedicamentType;
    @FXML
    public ChoiceBox choiceBoxMedicament;

    private DBInit dbInit;
    private ObservableList<String> itemsMedicament = FXCollections.<String>observableArrayList();
    private ObservableList<String> itemsMedicamentType = FXCollections.<String>observableArrayList();
    private Map<String, Integer> MedicamentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
        choiceBoxMedicament.setItems(itemsMedicament);
        choiceBoxMedicamentType.setItems(itemsMedicamentType);

        try {
            ResultSet setMedicamentType = connection.executeQueryAndGetResult("select distinct * from medicament_type");
            MedicamentType = new HashMap<>();
            itemsMedicamentType.clear();
            itemsMedicamentType.add("all");
            MedicamentType.put("all", 0);
            if (setMedicamentType != null) {
                while (setMedicamentType.next()) {
                    String name = setMedicamentType.getString(2);
                    Integer id = setMedicamentType.getInt(1);
                    MedicamentType.put(name, id);
                    itemsMedicamentType.add(name);
                }
            }

            ResultSet setMedicament = connection.executeQueryAndGetResult("select distinct title from medicament");
            itemsMedicament.clear();
            itemsMedicament.add("all");
            if (setMedicament != null) {
                while (setMedicament.next()) {
                    itemsMedicament.add(setMedicament.getString(1));
                }
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    boolean checkDate(String date1, String date2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateFormat1 = format.parse(date1);
        Date dateFormat2 = format.parse(date2);
        int result = date1.compareTo(date2);
        if (result == 0) {
            System.out.println("Date1 is equal to Date2");
            return false;
        } else if (result > 0) {
            System.out.println("Date1 is after Date2");
            return false;
        } else if (result < 0) {
            System.out.println("Date1 is before Date2");
            return true;
        }
        return false;
    }

    @FXML
    private void listButtonTapped() throws ParseException {
        if (startDate.getText().isEmpty() || endDate.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else if (!checkDate(startDate.getText(), endDate.getText())) {
            showAlert("wrong data", "");
        } else if (choiceBoxMedicament.getSelectionModel().isEmpty()) {
            String sql = "select patient_firstname, patient_surname " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "where ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        } else if (choiceBoxMedicament.getValue().toString().equals("all")) {
            String sql = "select patient_firstname, patient_surname " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "where ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        } else {
            String sql = "select patient_firstname, patient_surname " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "where med.title = '" + choiceBoxMedicament.getValue().toString() + "' and ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        }
    }

    @FXML
    private void numButtonTapped() throws ParseException {
        if (startDate.getText().isEmpty() || endDate.getText().isEmpty()) {
            showAlert("empty!", "Fill in required fields");
        } else if (!checkDate(startDate.getText(), endDate.getText())) {
            showAlert("wrong data", "");
        } else if (choiceBoxMedicament.getSelectionModel().isEmpty()) {
            String sql = "select count(*) as \"count of patients\" " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "where ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        } else if (choiceBoxMedicament.getValue().toString().equals("all")) {
            String sql = "select count(*) as \"count of patients\" " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "where ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        } else {
            String sql = "select count(*) as \"count of patients\" " +
                    "from patient pat " +
                    "left outer join making_an_order mao " +
                    "on pat.patient_id = mao.patn_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "where med.title = '" + choiceBoxMedicament.getValue().toString() + "' and ord.start_date > to_date('" +
                    startDate.getText() + "', 'DD-MM-YYYY') and ord.start_date < to_date('" +
                    endDate.getText() + "', 'DD-MM-YYYY')";
            showResult(sql);
        }

    }
}
