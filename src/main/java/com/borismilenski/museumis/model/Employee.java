package com.borismilenski.museumis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private final String webNiceName;

    public Employee(@JsonProperty("id") UUID id,
                    @JsonProperty("name") String name,
                    @JsonProperty("webNiceName") String webNiceName,
                    @JsonProperty("position") Position position) {
        this.id = id;
        this.name = name;
        this.webNiceName = webNiceName;
        this.position = position;
    }

    public Employee(UUID id, Employee employee) {
        this.id = id;
        this.name = employee.getName();
        this.webNiceName = employee.getWebNiceName();
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


    public String getWebNiceName() {
        return webNiceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPosition(), getWebNiceName());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", webNiceName='" + webNiceName + '\'' +
                '}';
    }
}
