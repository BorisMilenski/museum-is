package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleDAO{
    List<Employee> getScheduleForAllEmployees();

    Optional<Employee> getScheduleForEmployee(UUID employeeId);
}
