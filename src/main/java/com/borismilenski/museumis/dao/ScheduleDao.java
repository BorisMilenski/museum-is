package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Schedule;
import com.borismilenski.museumis.model.ScheduleSlot;
import org.apache.tomcat.jni.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository("scheduleDao")
public class ScheduleDao extends GenericDaoImpl<Schedule> {
    private final SlotDao slotDao;
    public ScheduleDao(JdbcTemplate jdbcTemplate, SlotDao slotDao) {
        super(jdbcTemplate);
        this.slotDao = slotDao;
    }

    public Optional<Schedule> findAll(LocalDate from, LocalDate to){
        Schedule schedule = new Schedule(UUID.randomUUID(), from, to, slotDao.findAllForPeriod(LocalDateTime.from(from.atStartOfDay()),LocalDateTime.from(to.atTime(23,59,59))));
        if (schedule.getSlots().size() > 0){
            return Optional.of(schedule);
        }
        return Optional.empty();
    }

    public Optional<Schedule> find(UUID id) {
        String selectQuery =  selectSingleInstanceSQL();
        return this.getJdbcTemplate().query(
                selectQuery,
                new Object[]{id.toString()},
                this.map()
        ).stream().findFirst();
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
