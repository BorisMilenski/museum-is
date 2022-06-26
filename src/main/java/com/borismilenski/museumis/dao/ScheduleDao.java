package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Schedule;
import com.borismilenski.museumis.model.ScheduleSlot;
import org.apache.tomcat.jni.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("scheduleDao")
public class ScheduleDao extends GenericDaoImpl<Schedule> {
    private final SlotDao slotDao;
    private final EmployeeDaoImpl employeeDao;
    public ScheduleDao(JdbcTemplate jdbcTemplate, SlotDao slotDao, EmployeeDaoImpl employeeDao) {
        super(jdbcTemplate);
        this.slotDao = slotDao;
        this.employeeDao = employeeDao;
    }

    public Optional<Schedule> findAll(LocalDate from, LocalDate to){
        Schedule schedule = new Schedule(UUID.randomUUID(), from, to, slotDao.findAllForPeriod(LocalDateTime.from(from.atStartOfDay()),LocalDateTime.from(to.atTime(23,59,59))));
        if (schedule.getSlots().size() > 0){
            return Optional.of(schedule);
        }
        return Optional.empty();
    }

    public Optional<Schedule> find(UUID employeeId, LocalDate from, LocalDate to) {
        Optional<Schedule> schedule = findAll(from, to);
        if (schedule.isPresent()){
            Optional<Employee> filter = employeeDao.find(employeeId);
            if (filter.isPresent()) {
                List<ScheduleSlot> slotsForEmployee = schedule.get().getSlots().stream()
                        .filter(scheduleSlot -> scheduleSlot.getEmployee().equals(filter.get()))
                        .collect(Collectors.toList());
                if (slotsForEmployee.size() > 0){
                    return Optional.of(new Schedule(UUID.randomUUID(), from, to, slotsForEmployee));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Schedule> find(String employeeWebNiceName, LocalDate from, LocalDate to){
        Optional<Employee> employee = employeeDao.find(employeeWebNiceName);
        if (employee.isPresent()){
            return this.find(employee.get().getId(), from, to);
        }
        return Optional.empty();
    }
    @Override
    public Schedule create(Schedule schedule) {
        schedule.getSlots().forEach(slotDao::create);
        return schedule;
    }

    @Override
    public int update(Schedule schedule) {
        final int[] total = {0};
        schedule.getSlots().forEach(scheduleSlot -> total[0] += slotDao.update(scheduleSlot));
        return total[0];
    }

    @Override
    String selectSingleInstanceSQL() {
        return null;
    }

    @Override
    String selectAllInstancesSQL() {
        return null;
    }

    @Override
    public RowMapper<Schedule> map() {
        return null;
    }
}
