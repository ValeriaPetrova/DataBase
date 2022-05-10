module ru.nsu.pharmacydatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.nsu.pharmacydatabase to javafx.fxml;
    exports ru.nsu.pharmacydatabase;
}