package ru.nsu.databaseproject.database.entities.order;

import lombok.Getter;
import lombok.Setter;

public class OrderEntity {
    @Getter @Setter
    private Integer orderId;
    @Getter @Setter
    private String structure;
    @Getter @Setter
    private Boolean isReady;
    @Getter @Setter
    private  Boolean isReceived;

    @Override
    public String toString() {
        return "orderId = " + orderId +
                ", structure = " + getStructure() +
                ", isReady = " + getIsReady() +
                ", isReceived = " + getIsReceived();
    }
}
