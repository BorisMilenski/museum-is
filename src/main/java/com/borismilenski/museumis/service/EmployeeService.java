package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDAO;
import com.borismilenski.museumis.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    @Autowired
    public EmployeeService(@Qualifier("employeeDAO") EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getAllEmployees(){
        return employeeDAO.getAllEmployees();
    }

    public Optional<Employee> getEmployee(UUID id){
        return employeeDAO.getEmployee(id);
    }

    public void updateEmployee(UUID id, Employee newEmployee){
        employeeDAO.updateEmployee(id,  newEmployee);
    }
    public void addEmployee(Employee employee){
        employeeDAO.addEmployee(employee);
    }
    public void addEmployee(UUID id, Employee employee){
        employeeDAO.addEmployee(id, employee);
    }

    public void deleteEmployee(UUID id){
        employeeDAO.deleteEmployee(id);
    }
}
