package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Schedule {
    private final UUID id;
    @NotNull
    private final LocalDate from;
    @NotNull
    private final LocalDate to;
    @NotNull
    private final List<ScheduleSlot> slots;

    public Schedule(@JsonProperty("scheduleId") UUID id,
                    @JsonProperty("from") LocalDate from,
                    @JsonProperty("to") LocalDate to,
                    @JsonProperty("slots") List<ScheduleSlot> slots) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.slots = slots;
    }

    public UUID getId() {
        return id;
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
