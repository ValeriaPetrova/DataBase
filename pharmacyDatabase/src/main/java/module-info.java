module ru.nsu.pharmacydatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.nsu.pharmacydatabase to javafx.fxml;
    exports ru.nsu.pharmacydatabase;
    exports ru.nsu.pharmacydatabase.roles;
    opens ru.nsu.pharmacydatabase.roles to javafx.fxml;
    exports ru.nsu.pharmacydatabase.requests;
    opens ru.nsu.pharmacydatabase.requests to javafx.fxml;
    exports ru.nsu.pharmacydatabase.controllerrs;
    opens ru.nsu.pharmacydatabase.controllerrs to javafx.fxml;
}