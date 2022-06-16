package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("employeeDao")
public class EmployeeDaoImpl extends GenericDaoImpl<Employee>{

    public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    String selectSingleInstanceSQL() {
        return  selectAllInstancesSQL() +
                "WHERE BIN_TO_UUID(id_employee) = ?" ;
    }

    @Override
    String selectAllInstancesSQL() {
        return  "" +
                "SELECT " +
                "BIN_TO_UUID(id_employee), " +
                "e.name, " +
                "BIN_TO_UUID(position_id), " +
                "p.base_pay, " +
                "p.days_off, " +
                "p.base_workload, " +
                "p.description, " +
                "p.name " +
                "FROM employees AS e " +
                "LEFT JOIN position AS p " +
                "ON position_id=id_position";
    }

    @Override
    public Employee create(Employee employee) {
        return null;
    }

    @Override
    public int update(Employee employee) {
        return 0;
    }

    @Override
    public RowMapper<Employee> map() {
        return (resultSet, i) -> {
            String employeeIdStr = resultSet.getString("BIN_TO_UUID(id_employee)");
            UUID employeeId = UUID.fromString(employeeIdStr);
            String employeeName = resultSet.getString(2);

            String positionIdStr = resultSet.getString("BIN_TO_UUID(position_id)");
            UUID positionId = UUID.fromString(positionIdStr);
            String name = resultSet.getString("p.name");
            float basePay = resultSet.getFloat("p.base_pay");
            int daysOff = resultSet.getInt("p.days_off");
            int workHoursPerWeek = resultSet.getInt("p.base_workload");
            String description = resultSet.getString("p.description");

            return new Employee(
                    employeeId,
                    employeeName,
                    new Position(
                            positionId,
                            name,
                            basePay,
                            daysOff,
                            workHoursPerWeek,
                            description)
            );
        };
    }
}
