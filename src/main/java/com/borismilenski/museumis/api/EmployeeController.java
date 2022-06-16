package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public void addNewEmployee(@RequestBody @Valid Employee employee){
        employeeService.create(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.findAll();
    }

    @GetMapping(path = "{employeeId}")
    public Optional<Employee> getEmployee(@PathVariable("employeeId") UUID id){
        return employeeService.find(id);
    }

    @PutMapping(path = "{employeeId}")
    public void updateEmployee(@RequestBody @Valid Employee newEmployee)
    {
        employeeService.update(newEmployee);
    }

    @DeleteMapping(path = "{employeeId}")
    public void deleteEmployee(@PathVariable("employeeId") UUID id){
        employeeService.delete(id);
    }

}
