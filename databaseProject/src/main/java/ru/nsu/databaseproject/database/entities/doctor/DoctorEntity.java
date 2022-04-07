package ru.nsu.databaseproject.database.entities.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DoctorEntity {
    @Getter @Setter
    private Integer doctorId;
    @Getter @Setter
    private String name;

    @Override
    public String toString() {
        return "doctorId = " + doctorId +
                ", name = " + getName();
    }
}
