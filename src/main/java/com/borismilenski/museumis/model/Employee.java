package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.Objects;
import java.util.UUID;

public final class Employee {

    private final UUID id;
    @NotBlank
    private final String name;
    @NotNull

    private final Position position;

    public Employee(@JsonProperty("id") UUID id,
                    @JsonProperty("name") String name,
                    @JsonProperty("position") Position position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Employee(UUID id, Employee employee) {
        this.id = id;
        this.name = employee.getName();
        this.position = employee.getPosition();
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Employee) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    @Override
    public String toString() {
        return "Employee[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "position=" + position + ']';
    }

}
