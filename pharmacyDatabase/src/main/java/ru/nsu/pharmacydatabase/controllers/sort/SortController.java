package ru.nsu.pharmacydatabase.controllers.sort;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import ru.nsu.pharmacydatabase.Main;
import ru.nsu.pharmacydatabase.controllers.select.SelectTableController;
import ru.nsu.pharmacydatabase.utils.Connection;
import ru.nsu.pharmacydatabase.utils.DBInit;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

public class SortController implements Initializable {
    @FXML
    private ChoiceBox colomnChoiceBox;
    @FXML
    private ChoiceBox typeSortChoiceBox;

    private Connection connection = Main.getConnection();
    private DBInit dbInit;
    private final LinkedList<TableColumn<Map, String>> columns = new LinkedList<>();
    private final ObservableList<String> columnNames = FXCollections .<String>observableArrayList();
    private ObservableList<String> itemsTypeSort = FXCollections .<String>observableArrayList();
    private String tableName = "patient";

    public void setListener(ChangeListener listener) {
    }

    public void showAlert(String message, String comment) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(comment);
        alert.showAndWait();
    }

    public void showResult(String sql) {
        try {
            ResultSet set = connection.executeQueryAndGetResult(sql);
            if (set != null) {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = null;
                try {
                    root = loader.load(getClass().getResourceAsStream(SelectTableController.fxml));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SelectTableController tableController = loader.getController();
                tableController.set(set);
                assert root != null;
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert("Ничего не найдено", "попробуйте поменять параметры поиска");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbInit = new DBInit(connection);
        colomnChoiceBox.setItems(columnNames);
        typeSortChoiceBox.setItems(itemsTypeSort);
        try {
            String operation = "SELECT * FROM " + tableName;
            ResultSet set = connection.executeQueryAndGetResult(operation);

            columnNames.clear();
            itemsTypeSort.clear();
            ResultSetMetaData metaData = set.getMetaData();
            int columnSize = set.getMetaData().getColumnCount();
            for(int i = 1; i <= columnSize; i++) {
                String columnName = metaData.getColumnName(i);
                TableColumn<Map, String> column = new TableColumn<>(columnName);
                column.setCellValueFactory(new MapValueFactory<>(columnName));
                column.setMinWidth(40);
                columns.add(column);
                columnNames.add(columnName);
            }
            itemsTypeSort.add("По возрастанию");
            itemsTypeSort.add("По убыванию");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchButtonTapped() {

    }
}
