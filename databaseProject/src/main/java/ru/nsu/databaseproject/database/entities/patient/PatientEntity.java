package ru.nsu.databaseproject.database.entities.patient;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class PatientEntity {
    @Getter @Setter
    private Integer patientId;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Date birthDate;
    @Getter @Setter
    private String phoneNumber;
    @Getter @Setter
    private String address;

    @Override
    public String toString() {
        return "patientId = " + patientId +
                ", name = " + getName() +
                ", birthDate = " + getBirthDate() +
                ", phoneNumber = " + getPhoneNumber() +
                ", address = " + getAddress();
    }
}
