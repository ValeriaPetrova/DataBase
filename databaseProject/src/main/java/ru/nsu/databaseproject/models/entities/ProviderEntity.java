package ru.nsu.databaseproject.models.entities;

import lombok.Getter;
import lombok.Setter;

public class ProviderEntity {
    private Integer providerId;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String product;

    @Override
    public String toString() {
        return "provider ID = " + providerId +
                ",\n provider address = " + address +
                ",\n provider product = " + product;
    }
}
