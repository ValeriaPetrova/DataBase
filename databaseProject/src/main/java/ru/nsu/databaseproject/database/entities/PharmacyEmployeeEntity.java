package ru.nsu.databaseproject.database.entities;

import lombok.Getter;
import lombok.Setter;

public class PharmacyEmployeeEntity {
    @Getter @Setter
    private Integer pharmacyEmployeeId;
    @Getter @Setter
    private String name;

    @Override
    public String toString() {
        return "pharmacyEmployeeId = " + pharmacyEmployeeId +
                ", name = " + getName();
    }
}
