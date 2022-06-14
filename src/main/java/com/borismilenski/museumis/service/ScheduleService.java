package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDAO;
import com.borismilenski.museumis.dao.ScheduleDAO;
import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Schedule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScheduleService {
    private final ScheduleDAO scheduleDAO;

    public ScheduleService(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    public List<Employee> getScheduleForAllEmployees() {
        return scheduleDAO.getScheduleForAllEmployees();
    }

    public Optional<Employee> getScheduleForEmployee(UUID employeeId) {
        return scheduleDAO.getScheduleForEmployee(employeeId);
    }
}
