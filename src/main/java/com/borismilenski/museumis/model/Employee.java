package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public final class Employee {
    private final UUID id;
    @NotBlank
    private final String name;
    @NotNull
    private final int workHoursPerWeek;
    @NotBlank
    private final String position;

    public Employee(@JsonProperty("id") UUID id,
                    @JsonProperty("name")String name,
                    @JsonProperty("workHoursPerWeek") int workHoursPerWeek,
                    @JsonProperty("position") String position) {
        this.id = id;
        this.name = name;
        this.workHoursPerWeek = workHoursPerWeek;
        this.position = position;
    }

    public Employee(UUID id, Employee employee) {
        this.id = id;
        this.name = employee.getName();
        this.workHoursPerWeek = employee.getWorkHoursPerWeek();
        this.position = employee.getPosition();
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWorkHoursPerWeek() {
        return workHoursPerWeek;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Employee) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                this.workHoursPerWeek == that.workHoursPerWeek &&
                Objects.equals(this.position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workHoursPerWeek, position);
    }

    @Override
    public String toString() {
        return "Employee[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "workHoursPerWeek=" + workHoursPerWeek + ", " +
                "position=" + position + ']';
    }

}
