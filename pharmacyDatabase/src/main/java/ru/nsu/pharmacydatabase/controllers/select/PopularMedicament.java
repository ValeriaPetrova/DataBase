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

public class PopularMedicament implements SelectController, Initializable {
    @FXML
    public Button listButton;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public ChoiceBox pageChoiceBox;

    private DBInit dbInit;
    private ObservableList<String> items = FXCollections.<String>observableArrayList();
    private ObservableList<String> itemsPage = FXCollections.<String>observableArrayList();
    private Map<String, Integer> MedicamentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbInit = new DBInit(connection);
        choiceBox.setItems(items);
        pageChoiceBox.setItems(itemsPage);
        try {
            ResultSet set = connection.executeQueryAndGetResult("select * from medicament_type");
            MedicamentType = new HashMap<>();
            items.clear();
            items.add("all");
            MedicamentType.put("all", 0);
            itemsPage.add("5");
            itemsPage.add("10");

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
        if (choiceBox.getSelectionModel().isEmpty() || pageChoiceBox.getSelectionModel().isEmpty()) {
            showAlert("empty!", "fields cannot be empty");
        } else if (MedicamentType.get(choiceBox.getValue().toString()) == 0 && pageChoiceBox.getValue().equals("5")) {
            String sql = "SELECT * FROM ( SELECT tmp.*, rownum rn " +
                    "FROM (select title " +
                    "from (select title, count(*) as c " +
                    "from medicament med " +
                    "left outer join order_ ord " +
                    "on ord.medicam_id = med.medicament_id " +
                    "group by title " +
                    "order by c desc " +
                    ") " +
                    ") tmp " +
                    "WHERE rownum <= 5) WHERE rn > 0";
            showResult(sql);
        } else if (MedicamentType.get(choiceBox.getValue().toString()) == 0 && pageChoiceBox.getValue().equals("10")) {
            String sql = "SELECT * FROM ( SELECT tmp.*, rownum rn " +
                    "FROM (select title " +
                    "from (select title, count(*) as c " +
                    "from medicament med " +
                    "left outer join order_ ord " +
                    "on ord.medicam_id = med.medicament_id " +
                    "group by title " +
                    "order by c desc " +
                    ") " +
                    ") tmp " +
                    "WHERE rownum <= 10) WHERE rn > 0";
            showResult(sql);
        } else if (pageChoiceBox.getValue().equals("5")){
            String sql = "SELECT * FROM ( SELECT tmp.*, rownum rn " +
                    "FROM ( select title, type_name, c as count" +
                                    "from (select med.title, med.type_id, count(*) as c " +
                                            "from medicament med " +
                                            "left outer join order_ ord " +
                                            "on ord.medicam_id = med.medicament_id " +
                                            "group by title, type_id " +
                                            "order by c desc " +
                                            ") " +
                                            "inner join medicament_type mt " +
                                            "on mt.medicament_type_id = type_id " +
                                            "where type_name = '" +
                    choiceBox.getValue().toString() + "'" +
                    ") tmp " +
                    "WHERE rownum <= 5) WHERE rn > 0";
            showResult(sql);
        } else if (pageChoiceBox.getValue().equals("10")){
            String sql = "SELECT * FROM ( SELECT tmp.*, rownum rn " +
                    "FROM ( select title, type_name, c as count" +
                    "from (select med.title, med.type_id, count(*) as c " +
                    "from medicament med " +
                    "left outer join order_ ord " +
                    "on ord.medicam_id = med.medicament_id " +
                    "group by title, type_id " +
                    "order by c desc " +
                    ") " +
                    "inner join medicament_type mt " +
                    "on mt.medicament_type_id = type_id " +
                    "where type_name = '" +
                    choiceBox.getValue().toString() + "'" +
                    ") tmp " +
                    "WHERE rownum <= 10) WHERE rn > 0";
            showResult(sql);
        }

    }

}
