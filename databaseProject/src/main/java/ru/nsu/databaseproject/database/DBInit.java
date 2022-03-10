package ru.nsu.databaseproject.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBInit {
    private static DBInit instance;
    @Getter
    private static Connection connection;
    private DBRequests dbRequests;
    private String url;

    public Connection startConnection(String url, String username, String password) throws SQLException {
        this.url = url;
        dbRequests = DBRequests.getInstance();
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        connection = DriverManager.getConnection(url, properties);
        return connection;
    }

    public void init() {
        drop();
        createTables();
        setData();
    }

    public void drop() {

    }

    public void createTables() {
        PreparedStatement preparedStatement = null;

        String sqlProviderTable = "CREATE TABLE provider (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    address VARCHAR(128) NOT NULL,\n" +
                "    products VARCHAR(512) NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlProviderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create provider table!");
            e.printStackTrace();
        }

        String sqlRequestTable = "CREATE TABLE request (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    provider_id BIGINT NOT NULL UNIQUE,\n" +
                "    FOREIGN KEY (provider_id) REFERENCES provider(id)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlRequestTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create request table!");
            e.printStackTrace();
        }

        String sqlMedicamentTypeTable = "CREATE TABLE medicament_type (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    type_name VARCHAR(128) NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlMedicamentTypeTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create medicament_type table!");
            e.printStackTrace();
        }

        String sqlPatientTable = "CREATE TABLE patient (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    name VARCHAR(128) NOT NULL ,\n" +
                "    birth_date DATE,\n" +
                "    phone_number CHAR(11) UNIQUE NOT NULL ,\n" +
                "    address VARCHAR(128)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlPatientTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create patient table!");
            e.printStackTrace();
        }

        String sqlDoctorTable = "CREATE TABLE doctor (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    name VARCHAR(128) NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlDoctorTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create doctor table!");
            e.printStackTrace();
        }

        String sqlPrescriptionTable = "CREATE TABLE prescription (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    number_of_medicines BIGINT,\n" +
                "    direction_for_use VARCHAR(128),\n" +
                "    diagnosis VARCHAR(128) NOT NULL ,\n" +
                "    doctor_id BIGINT,\n" +
                "    FOREIGN KEY (doctor_id) REFERENCES doctor(id),\n" +
                "    patient_id BIGINT,\n" +
                "    FOREIGN KEY (patient_id) REFERENCES patient(id)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlPrescriptionTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create prescription table!");
            e.printStackTrace();
        }

        String sqlMedicamentTable = "CREATE TABLE medicament (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    title VARCHAR(128) NOT NULL ,\n" +
                "    usage VARCHAR(128) NOT NULL ,\n" +
                "    production_time INTERVAL,\n" +
                "    shelf_life DATE NOT NULL ,\n" +
                "    critical_rate BIGINT NOT NULL ,\n" +
                "    actual_balance BIGINT NOT NULL ,\n" +
                "    price NUMERIC(10, 2) NOT NULL ,\n" +
                "    type_id BIGINT NOT NULL,\n" +
                "    FOREIGN KEY (type_id) REFERENCES medicament_type(id),\n" +
                "    prescription_id BIGINT,\n" +
                "    FOREIGN KEY (prescription_id) REFERENCES prescription(id)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlMedicamentTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create medicament table!");
            e.printStackTrace();
        }

        String sqlRequestStructureTable = "CREATE TABLE request_structure (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    medicament_id BIGINT NOT NULL UNIQUE,\n" +
                "    FOREIGN KEY (medicament_id) REFERENCES medicament(id),\n" +
                "    request_id BIGINT NOT NULL,\n" +
                "    FOREIGN KEY (request_id) REFERENCES request(id),\n" +
                "    amount BIGINT NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlRequestStructureTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create request_structure table!");
            e.printStackTrace();
        }

        String sqlHandbookOfDrugPreparationTechnologiesTable = "CREATE TABLE handbook_of_drug_preparation_technologies (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    medicament_id BIGINT NOT NULL UNIQUE,\n" +
                "    FOREIGN KEY (medicament_id) REFERENCES medicament(id),\n" +
                "    method_of_preparation VARCHAR(128) NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlHandbookOfDrugPreparationTechnologiesTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create handbook_of_drug_preparation_technologies table!");
            e.printStackTrace();
        }

        String sqlExamTable = "CREATE TABLE exam (\n" +
                "    patient_id BIGINT,\n" +
                "    FOREIGN KEY (patient_id) REFERENCES patient(id),\n" +
                "    doctor_id BIGINT,\n" +
                "    FOREIGN KEY (doctor_id) REFERENCES doctor(id),\n" +
                "    PRIMARY KEY (patient_id, doctor_id)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlExamTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create exam table!");
            e.printStackTrace();
        }

        String sqlPharmacyEmployeeTable = "CREATE TABLE pharmacy_employee (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    name VARCHAR(128)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlPharmacyEmployeeTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create pharmacy_employee table!");
            e.printStackTrace();
        }

        String sqlOrderTable = "CREATE TABLE _order (\n" +
                "    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
                "    structure VARCHAR(128) NOT NULL ,\n" +
                "    is_ready BOOLEAN NOT NULL ,\n" +
                "    is_received BOOLEAN NOT NULL\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlOrderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create _order table!");
            e.printStackTrace();
        }

        String sqlMakingAnOrderTable = "CREATE TABLE making_an_order (\n" +
                "    order_id BIGINT,\n" +
                "    FOREIGN KEY (order_id) REFERENCES _order(id),\n" +
                "    pharmacy_employee_id BIGINT,\n" +
                "    FOREIGN KEY (pharmacy_employee_id) REFERENCES pharmacy_employee(id),\n" +
                "    patient_id BIGINT,\n" +
                "    FOREIGN KEY (patient_id) REFERENCES patient(id),\n" +
                "    PRIMARY KEY (order_id, pharmacy_employee_id, patient_id)\n" +
                ");";
        try {
            preparedStatement = connection.prepareStatement(sqlMakingAnOrderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't create making_an_order table!");
            e.printStackTrace();
        }
    }

    private void setData() {
        
    }

}
