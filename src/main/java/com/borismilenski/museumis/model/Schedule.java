package com.borismilenski.museumis.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Schedule {
    private final LocalDate from;
    private final LocalDate to;
    private final Set<UUID> employeeIDs = new HashSet<>();
    private final List<ScheduleSlot> slots;

    public Schedule(LocalDate from, LocalDate to, List<ScheduleSlot> slots) {
        this.from = from;
        this.to = to;
        this.slots = slots;
    }

    public Set<UUID> getEmployeeIDs() {
        if(employeeIDs.isEmpty()){
            slots.stream()
                    .forEach((slot)->employeeIDs.add(slot.employee().getId()));
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
