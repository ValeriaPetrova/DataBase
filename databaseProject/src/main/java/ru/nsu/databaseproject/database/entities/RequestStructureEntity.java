package ru.nsu.databaseproject.database.entities;

import lombok.Getter;
import lombok.Setter;

public class RequestStructureEntity {
    @Getter @Setter
    private Integer requestStructureId;

    @Override
    public String toString() {
        return "requestStructureId = " + requestStructureId;
    }
}
