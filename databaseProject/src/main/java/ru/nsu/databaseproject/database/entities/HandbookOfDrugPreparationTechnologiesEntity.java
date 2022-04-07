package ru.nsu.databaseproject.database.entities;

import lombok.Getter;
import lombok.Setter;

public class HandbookOfDrugPreparationTechnologiesEntity {
    @Getter @Setter
    private Integer handbookOfDrugPreparationTechnologiesId;
    @Getter @Setter
    private String methodOfPreparation;

    @Override
    public String toString() {
        return "handbookOfDrugPreparationTechnologiesId = " + handbookOfDrugPreparationTechnologiesId +
                ", methodOfPreparation = " + getMethodOfPreparation();
    }
}
