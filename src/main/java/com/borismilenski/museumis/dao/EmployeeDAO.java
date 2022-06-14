package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeDAO {
    int addEmployee(UUID id, Employee employee);
    default int addEmployee(Employee employee){
        UUID id = UUID.randomUUID();
        return addEmployee(id, employee);
    }

    Optional<Employee> getEmployee(UUID id);
    List<Employee> getAllEmployees();
    int updateEmployee(UUID id, Employee newEmployee);
    int deleteEmployee(UUID id);
}
