package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("scheduleDAO")
public class TempScheduleDAO implements ScheduleDAO{
    @Override
    public List<Employee> getScheduleForAllEmployees() {
        return null;
    }

    @Override
    public Optional<Employee> getScheduleForEmployee(UUID employeeId) {
        return Optional.empty();
    }
}
