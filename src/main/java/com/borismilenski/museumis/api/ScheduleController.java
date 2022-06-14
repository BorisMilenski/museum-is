package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/employees/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Employee> getScheduleForAllEmployees(){
        return scheduleService.getScheduleForAllEmployees();
    }

    @GetMapping(path = "{employeeId}")
    public Optional<Employee> getScheduleForEmployee(@PathVariable("employeeId") UUID employeeId){
        return scheduleService.getScheduleForEmployee(employeeId);
    }

    @PutMapping(path = "{employeeId}")
    public void updateSchedule(@PathVariable("employeeId") UUID id, @RequestBody @Valid Employee newEmployee)
    {
        //
    }
}
