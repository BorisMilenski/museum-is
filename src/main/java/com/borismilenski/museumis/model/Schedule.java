package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Schedule {
    @NotNull
    private final LocalDate from;
    @NotNull
    private final LocalDate to;
    @NotNull
    private final List<ScheduleSlot> slots;
    private final Set<UUID> employeeIDs = new HashSet<>();

    public Schedule(@JsonProperty("from") LocalDate from,
                    @JsonProperty("to") LocalDate to,
                    @JsonProperty("slots") List<ScheduleSlot> slots) {
        this.from = from;
        this.to = to;
        this.slots = slots;
    }

    public Set<UUID> getEmployeeIDs() {
        if(employeeIDs.isEmpty()){
            slots.stream()
                    .forEach((slot)->employeeIDs.add(slot.getEmployee().getId()));
        }
        return employeeIDs;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public List<ScheduleSlot> getSlots() {
        return slots;
    }
}
