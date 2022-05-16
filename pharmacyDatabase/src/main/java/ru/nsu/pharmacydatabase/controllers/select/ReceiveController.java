package ru.nsu.pharmacydatabase.controllers.select;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReceiveController implements SelectController {
    @FXML
    public Button listButton;
    @FXML
    public Button numButton;

    @FXML
    private void listButtonTapped() {
        String sql = "select patient_firstname, patient_surname, patient_birthdate, patient_phone_number, patient_address, registration_date " +
                "from patient pat " +
                "inner join making_an_order mao " +
                "on mao.patn_id = pat.patient_id " +
                "inner join order_ " +
                "on order_.order_id = mao.order_id " +
                "where order_.is_ready = 'YES' and order_.is_received = 'NO' ";
        showResult(sql);
    }

    @FXML
    private void numButtonTapped() {
        String sql = "select count(*) as \"count of patients\" " +
                "from patient pat " +
                "inner join making_an_order mao " +
                "on mao.patn_id = pat.patient_id " +
                "inner join order_ " +
                "on order_.order_id = mao.order_id " +
                "where order_.is_ready = 'YES' and order_.is_received = 'NO' ";
        showResult(sql);
    }
}
