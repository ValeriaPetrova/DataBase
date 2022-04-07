package ru.nsu.databaseproject.database.entities;

import lombok.Getter;
import lombok.Setter;

public class RequestEntity {
    @Getter @Setter
    private Integer requestId;

    @Override
    public String toString() {
        return "requestId = " + requestId;
    }
}
