package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class ScheduleSlot {
    private final UUID id;
    @NotNull
    private final LocalDateTime from;
    @NotNull
    private final LocalDateTime to;
    @NotNull
    private final Employee employee;

    public ScheduleSlot(@JsonProperty("slotId") UUID id,
                        @JsonProperty("from") LocalDateTime from,
                        @JsonProperty("to") LocalDateTime to,
                        @JsonProperty("employee") Employee employee) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.employee = employee;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ScheduleSlot) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.from, that.from) &&
                Objects.equals(this.to, that.to) &&
                Objects.equals(this.employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, employee);
    }

    @Override
    public String toString() {
        return "ScheduleSlot[" +
                "id=" + id + ", " +
                "from=" + from + ", " +
                "to=" + to + ", " +
                "employee=" + employee + ']';
    }

}
