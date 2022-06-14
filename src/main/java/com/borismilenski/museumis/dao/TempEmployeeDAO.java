package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("employeeDAO")
public class TempEmployeeDAO implements EmployeeDAO{
//    private final JdbcTemplate jdbcTemplate;
//    public TempEmployeeDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
    private final List<Employee> DB = new ArrayList<>();



    @Override
    public Optional<Employee> getEmployee(UUID id) {
        return DB.stream().filter((e) -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return DB;
    }
    @Override
    public int addEmployee(UUID id, Employee employee) {
        DB.add(new Employee(id, employee));
        return 0;
    }
    @Override
    public int updateEmployee(UUID id, Employee newEmployee) {
        return 0;
    }

    @Override
    public int deleteEmployee(UUID id) {
        return 0;
    }
}
