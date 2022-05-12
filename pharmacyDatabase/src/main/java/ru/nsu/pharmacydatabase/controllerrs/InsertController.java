package ru.nsu.pharmacydatabase.controllerrs;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;
import ru.nsu.pharmacydatabase.Connection;
import ru.nsu.pharmacydatabase.InsertMode;
import ru.nsu.pharmacydatabase.Main;

public interface InsertController {
    Connection connection = Main.getConnection();

    default void setListener(ChangeListener listener) {
    }

    default void showAlert(String message, String comment) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(comment);
        alert.showAndWait();
    }

    void setMode(InsertMode mode);
    void setItem(String item);
}
