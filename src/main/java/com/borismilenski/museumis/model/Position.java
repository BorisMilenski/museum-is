package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public class Position {
    private final UUID id;
    @NotBlank
    private final String name;
    @NotNull
    private final float basePay;
    @NotNull
    private final int daysOff;
    @NotNull
    private final int workHoursPerWeek;
    @NotNull
    private final String description;

    private final int maxOnShift;

    private final boolean canWorkLessHours;


    public Position(@JsonProperty("id") UUID id,
                    @JsonProperty("name") String name,
                    @JsonProperty("basePay") float basePay,
                    @JsonProperty("daysOff") int daysOff,
                    @JsonProperty("workHoursPerWeek") int workHoursPerWeek,
                    @JsonProperty("description") String description,
                    @JsonProperty("maxOnShift") int maxOnShift,
                    @JsonProperty("canWorkLessHours") boolean canWorkLessHours) {
        this.id = id;
        this.name = name;
        this.basePay = basePay;
        this.daysOff = daysOff;
        this.workHoursPerWeek = workHoursPerWeek;
        this.description = description;
        this.maxOnShift = maxOnShift;
        this.canWorkLessHours = canWorkLessHours;
    }

    public Position(UUID id, Position newPosition) {
        this.id = id;
        this.name = newPosition.getName();
        this.basePay = newPosition.getBasePay();
        this.daysOff = newPosition.getDaysOff();
        this.workHoursPerWeek = newPosition.getWorkHoursPerWeek();
        this.description = newPosition.getDescription();
        this.maxOnShift = newPosition.getMaxOnShift();
        this.canWorkLessHours = newPosition.isCanWorkLessHours();
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBasePay() {
        return basePay;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public int getWorkHoursPerWeek() {
        return workHoursPerWeek;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxOnShift() {
        return maxOnShift;
    }

    public boolean isCanWorkLessHours() {
        return canWorkLessHours;
    }
}
