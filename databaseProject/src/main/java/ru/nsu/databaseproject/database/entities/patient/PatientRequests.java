package ru.nsu.databaseproject.database.entities.patient;

import lombok.Setter;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class PatientRequests {
    @Setter
    private Connection connection;

    public String toDate(Date date) {
        if (date == null) {
            return "TO_DATE (null, 'dd.mm.yyyy')";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedString = date.toLocalDate().format(formatter);
        return "TO_DATE ( '" + formattedString + "', 'dd.mm.yyyy')";
    }

    public void addPatient(PatientEntity patient) {
        String sqlAddPatient = "INSERT INTO pharmacyAdmin.patient_table VALUES (" +
                "'" + patient.getPatientId() + "' ," +
                patient.getName() + ", " +
                toDate(patient.getBirthDate()) + ", " +
                patient.getPhoneNumber() + ", " +
                patient.getAddress() +
                ")";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAddPatient);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't add patient");
            e.printStackTrace();
        }
    }

    public void deletePatient(Integer patientId) {
        String sqlDeletePatient = "DELETE FROM pharmacyAdmin.patient_table WHERE patient_id=" + patientId;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeletePatient);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete patient");
            e.printStackTrace();
        }
    }

    public void updatePatient(PatientEntity patient) {
        String sqlUpdatePatient = "UPDATE pharmacyAdmin.patient_table SET " +
                "patient_name= " + "'" + patient.getName() + "' " +
                ", patient_birthdate= " + toDate(patient.getBirthDate()) + " " +
                ", patient_phone_number= " + "'" + patient.getPhoneNumber() + "' " +
                ", patient_address= " + "'" + patient.getAddress() + "' " +
                "WHERE patient_id= " + patient.getPatientId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdatePatient);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update patient");
            e.printStackTrace();
        }
    }

    public List<PatientEntity> getPatients() {
        String sqlGetPatients = "SELECT * from pharmacyAdmin.patient_table ";
        List<PatientEntity> patients= new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetPatients);
            ResultSet resultSet = preparedStatement.executeQuery(sqlGetPatients);
            while (resultSet.next()) {
                PatientEntity patient = new PatientEntity();
                patient.setPatientId(resultSet.getInt("patient_id"));
                patient.setName(resultSet.getString("patient_name"));
                Date birthDate = resultSet.getDate("patient_birthdate");
                if (birthDate != null) {
                    patient.setBirthDate(birthDate);
                }
                patient.setPhoneNumber(resultSet.getString("patient_phone_number"));
                patient.setAddress(resultSet.getString("patient_address"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Can't get list of patients");
            e.printStackTrace();
        }
        return patients;
    }
}
