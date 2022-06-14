package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDAO;
import com.borismilenski.museumis.dao.ScheduleDAO;
import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDAO;

    @Autowired
    public ScheduleService(@Qualifier("scheduleDAO") ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    public List<Employee> getScheduleForAllEmployees() {
        return scheduleDAO.getScheduleForAllEmployees();
    }

    public Optional<Employee> getScheduleForEmployee(UUID employeeId) {
        return scheduleDAO.getScheduleForEmployee(employeeId);
    }
}
