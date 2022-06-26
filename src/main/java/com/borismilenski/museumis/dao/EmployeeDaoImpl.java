package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("employeeDao")
public class EmployeeDaoImpl extends GenericDaoImpl<Employee>{

    public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Optional<Employee> find(String employeeWebNiceName) {
        String selectQuery =  selectAllInstancesSQL().concat("WHERE e.website_nice_name = ?");
        return this.getJdbcTemplate().query(
                selectQuery,
                new Object[]{employeeWebNiceName},
                this.map()
        ).stream().findFirst();
    }
    @Override
    String selectSingleInstanceSQL() {
        return  selectAllInstancesSQL() +
                "WHERE id_employee = UUID_TO_BIN(?)" ;
    }

    @Override
    String selectAllInstancesSQL() {
        return  "" +
                "SELECT " +
                "BIN_TO_UUID(id_employee), " +
                "e.name, " +
                "e.website_nice_name, " +
                "BIN_TO_UUID(position_id), " +
                "p.base_pay, " +
                "p.days_off, " +
                "p.base_workload, " +
                "p.description, " +
                "p.name, " +
                "p.max_on_shift, " +
                "p.can_work_less_hours " +
                "FROM employees AS e " +
                "LEFT JOIN position AS p " +
                "ON position_id=id_position ";
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
            String employeeName = resultSet.getString("e.name");
            String employeeWebNiceName = resultSet.getString("e.website_nice_name");

            String positionIdStr = resultSet.getString("BIN_TO_UUID(position_id)");
            UUID positionId = UUID.fromString(positionIdStr);
            String name = resultSet.getString("p.name");
            float basePay = resultSet.getFloat("p.base_pay");
            int daysOff = resultSet.getInt("p.days_off");
            int workHoursPerWeek = resultSet.getInt("p.base_workload");
            String description = resultSet.getString("p.description");
            int maxOnShift = resultSet.getInt("p.max_on_shift");
            boolean canWorkLessHours = resultSet.getBoolean("p.can_work_less_hours");

            return new Employee(
                    employeeId,
                    employeeName,
                    employeeWebNiceName,
                    new Position(
                            positionId,
                            name,
                            basePay,
                            daysOff,
                            workHoursPerWeek,
                            description,
                            maxOnShift,
                            canWorkLessHours)
            );
        };
    }
}
