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
            String sql = "select title " +
                    "from (select title, count(*) as c " +
                    "from medicament med " +
                    "left outer join order_ ord " +
                    "on ord.medicam_id = med.medicament_id " +
                    "group by title " +
                    "order by c desc " +
                    ") " +
                    "where ROWNUM < 11";
            showResult(sql);
        } else {
            String sql = "select title, type_name, c " +
                    "from (select med.title, med.type_id, count(*) as c " +
                    "from medicament med " +
                    "left outer join order_ ord " +
                    "on ord.medicam_id = med.medicament_id " +
                    "group by title, type_id " +
                    "order by c desc " +
                    ") " +
                    "inner join medicament_type mt " +
                    "on mt.medicament_type_id = type_id " +
                    "where ROWNUM < 11 and type_name = '" + choiceBox.getValue().toString() + "'";
            showResult(sql);
        }
    }

}
