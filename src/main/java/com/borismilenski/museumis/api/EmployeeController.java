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
        employeeService.addEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping(path = "{employeeId}")
    public Optional<Employee> getEmployee(@PathVariable("employeeId") UUID id){
        return employeeService.getEmployee(id);
    }

    @PutMapping(path = "{employeeId}")
    public void updateEmployee(@PathVariable("employeeId") UUID id, @RequestBody @Valid Employee newEmployee)
    {
        employeeService.updateEmployee(id, newEmployee);
    }

    @DeleteMapping(path = "{employeeId}")
    public void deleteEmployee(@PathVariable("employeeId") UUID id){
        employeeService.deleteEmployee(id);
    }

}
