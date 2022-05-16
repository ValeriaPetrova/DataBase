package ru.nsu.pharmacydatabase.controllers.select;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PatientsWaitingToOrder implements SelectController, Initializable {
    @FXML
    public Button listButton;
    @FXML
    public ChoiceBox choiceBox;
    private DBInit dbInit;
    private ObservableList<String> items = FXCollections.<String>observableArrayList();
    private Map<String, Integer> MedicamentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
        choiceBox.setItems(items);

        try {
            ResultSet set = connection.executeQueryAndGetResult("select * from medicament_type");
            MedicamentType = new HashMap<>();
            items.clear();
            items.add("all");
            MedicamentType.put("all", 0);
            if (set != null) {
                while (set.next()) {
                    String name = set.getString(2);
                    Integer id = set.getInt(1);
                    MedicamentType.put(name, id);
                    items.add(name);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void listButtonTapped() {
        if (MedicamentType.get(choiceBox.getValue().toString()) == 0) {
            String sql = "select patient_firstname, patient_surname " +
                    "from patient pat " +
                    "inner join making_an_order mao " +
                    "on mao.patn_id = pat.patient_id " +
                    "inner join order_ " +
                    "on order_.order_id = mao.order_id " +
                    "where order_.is_ready = 'NO'";
            showResult(sql);
        } else {
            String sql = "select patient_firstname, patient_surname " +
                    "from patient pat " +
                    "inner join making_an_order mao " +
                    "on mao.patn_id = pat.patient_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "inner join medicament_type mt " +
                    "on mt.medicament_type_id = med.type_id " +
                    "where ord.is_ready = 'NO' and mt.type_name = '" + choiceBox.getValue().toString() + "'";
            showResult(sql);
        }
    }

    @FXML
    private void numButtonTapped() {
        if (MedicamentType.get(choiceBox.getValue().toString()) == 0) {
            String sql = "select count(*) as \"count of patients\" " +
                    "from patient pat " +
                    "inner join making_an_order mao " +
                    "on mao.patn_id = pat.patient_id " +
                    "inner join order_ " +
                    "on order_.order_id = mao.order_id " +
                    "where order_.is_ready = 'NO'";
            showResult(sql);
        } else {
            String sql = "select count(*) as \"count of patients\" " +
                    "from patient pat " +
                    "inner join making_an_order mao " +
                    "on mao.patn_id = pat.patient_id " +
                    "inner join order_ ord " +
                    "on ord.order_id = mao.order_id " +
                    "inner join medicament med " +
                    "on med.medicament_id = ord.medicam_id " +
                    "inner join medicament_type mt " +
                    "on mt.medicament_type_id = med.type_id " +
                    "where ord.is_ready = 'NO' and mt.type_name = '" + choiceBox.getValue().toString() + "'";
            showResult(sql);
        }
    }
}
