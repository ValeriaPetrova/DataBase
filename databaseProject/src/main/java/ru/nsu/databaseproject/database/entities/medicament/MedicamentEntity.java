package ru.nsu.databaseproject.database.entities.medicament;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

public class MedicamentEntity {
    @Getter @Setter
    private Integer medicamentId;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private MedicamentType type;
    @Getter @Setter
    private String usage;
    @Getter @Setter
    private Time productionTime;
    @Getter @Setter
    private Date shelfLife;
    @Getter @Setter
    private Integer criticalRate;
    @Getter @Setter
    private Integer actualBalance;
    @Getter @Setter
    private Double price;

    @Override
    public String toString() {
        return "medicamentId = " + medicamentId +
                ", title = " + getTitle() +
                ", type = " +getType() +
                ", usage = " + getUsage() +
                ", productionTime = " + getProductionTime() +
                ", shelfLife = " + getShelfLife() +
                ", criticalRate = " + getCriticalRate() +
                ", actualBalance = " + getActualBalance() +
                ", price = " + getPrice();
    }
}
