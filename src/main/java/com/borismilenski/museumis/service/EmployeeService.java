package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDaoImpl;
import com.borismilenski.museumis.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService extends GenericService<Employee>{
    @Autowired
    public EmployeeService(@Qualifier("employeeDao") EmployeeDaoImpl employeeDAO) {
        super(employeeDAO);
    }

    public Optional<Employee> find(String employeeWebNiceName){
        return ((EmployeeDaoImpl)this.getGenericDao()).find(employeeWebNiceName);
    }
}
