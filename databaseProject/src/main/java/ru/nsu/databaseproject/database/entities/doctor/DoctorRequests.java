package ru.nsu.databaseproject.database.entities.doctor;

import lombok.Setter;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DoctorRequests {
    @Setter
    private Connection connection;

    public void addDoctor(DoctorEntity doctor) {
        String sqlAddDoctor = "INSERT INTO pharmacyAdmin.doctor_table VALUES (" +
                "'" + doctor.getDoctorId() + "' ," +
                doctor.getName() +
                ")";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAddDoctor);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't add doctor");
            e.printStackTrace();
        }
    }

    public void deleteDoctor(Integer doctorId) {
        String sqlDeleteDoctor = "DELETE FROM pharmacyAdmin.doctor_table WHERE doctor_id=" + doctorId;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteDoctor);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete doctor");
            e.printStackTrace();
        }
    }

    public void updateDoctor(DoctorEntity doctor) {
        String sqlUpdateDoctor = "UPDATE pharmacyAdmin.doctor_table SET " +
                "doctor_name= " + "'" + doctor.getName() + "' " +
                "WHERE doctor_id= " + doctor.getDoctorId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateDoctor);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update doctor");
            e.printStackTrace();
        }
    }

    public List<DoctorEntity> getDoctors() {
        String sqlGetDoctors = "SELECT * FROM pharmacyAdmin.doctor_table ";
        List<DoctorEntity> doctors = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetDoctors);
            ResultSet resultSet = preparedStatement.executeQuery(sqlGetDoctors);
            while (resultSet.next()) {
                DoctorEntity doctor = new DoctorEntity();
                doctor.setDoctorId(resultSet.getInt("doctor_id"));
                doctor.setName(resultSet.getString("doctor_name"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Can't get list of doctors");
            e.printStackTrace();
        }
        return doctors;
    }
}
