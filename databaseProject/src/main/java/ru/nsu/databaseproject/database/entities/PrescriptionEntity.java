package ru.nsu.databaseproject.database.entities;

import lombok.Getter;
import lombok.Setter;

public class PrescriptionEntity {
    @Getter @Setter
    private Integer prescriptionId;
    @Getter @Setter
    private Integer numberOfMedicines;
    @Getter @Setter
    private String directionForUse;
    @Getter @Setter
    private String diagnosis;

    @Override
    public String toString() {
        return  "prescriptionId = " + prescriptionId +
                ", numberOfMedicines = " + getNumberOfMedicines() +
                ", directionForUse = " + getDirectionForUse() +
                ", diagnosis = " + getDiagnosis();
    }
}
