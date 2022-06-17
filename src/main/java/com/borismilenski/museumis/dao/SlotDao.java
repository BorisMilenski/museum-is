package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Position;
import com.borismilenski.museumis.model.ScheduleSlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository("slotDao")
public class SlotDao extends GenericDaoImpl<ScheduleSlot>{
    public SlotDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
    public List<ScheduleSlot> findAllforEmployee(UUID id){
        String selectQuery = selectSingleInstanceSQL();
        return jdbcTemplate.query(
                selectQuery,
                new Object[]{id.toString()},
                this.map());
    }
    @Override
    public ScheduleSlot create(ScheduleSlot scheduleSlot) {
        String sql = "INSERT INTO  museum_is.schedule id_schedule, employee_id, time_start, time_end, pay_mod) VALUES (UUID_TO_BIN( ? ), UUID_TO_BIN( ? ), ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                scheduleSlot.getId(),
                scheduleSlot.getEmployee().getId(),
                scheduleSlot.getFrom(),
                scheduleSlot.getTo()
        );
        return scheduleSlot;
    }

    @Override
    public int update(ScheduleSlot scheduleSlot) {
        String sql = "UPDATE schedule SET " +
                "employee_id = UUID_TO_BIN( ? ), " +
                "time_start = ?, " +
                "time_end = ?, " +
                "WHERE BIN_TO_UUID(id_schedule) = ? ";
        return jdbcTemplate.update(
                sql,
                scheduleSlot.getEmployee().getId(),
                scheduleSlot.getFrom(),
                scheduleSlot.getTo(),
                scheduleSlot.getId()
        );
    }
    @Override
    String selectSingleInstanceSQL() {
        return selectAllInstancesSQL()+ "WHERE employee_id = UUID_TO_BIN(?) ";
    }

    @Override
    String selectAllInstancesSQL() {
        return "SELECT BIN_TO_UUID(id_schedule), BIN_TO_UUID(employee_id), time_start, time_end, pay_mod FROM museum_is.schedule ";
    }

    @Override
    public RowMapper<ScheduleSlot> map() {
        return (resultSet, i) -> {
            String slotIdStr = resultSet.getString("BIN_TO_UUID(id_schedule)");
            UUID slotId = UUID.fromString(slotIdStr);

            String employeeIdStr = resultSet.getString("BIN_TO_UUID(employee_id)");
            UUID employeeId = UUID.fromString(employeeIdStr);

            LocalDateTime from = resultSet.getObject("time_start", LocalDateTime.class);
            LocalDateTime to = resultSet.getObject("time_end", LocalDateTime.class);

            Employee employee = new EmployeeDaoImpl(jdbcTemplate).find(employeeId).get();

            return new ScheduleSlot(
                    slotId,
                    from,
                    to,
                    employee

            );
        };
    }
}
