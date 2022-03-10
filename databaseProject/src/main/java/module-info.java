module ru.nsu.databaseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;


    opens ru.nsu.databaseproject to javafx.fxml;
    exports ru.nsu.databaseproject;
}