package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("scheduleDao")
public class ScheduleDao extends GenericDaoImpl<Schedule> {
    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Schedule create(Schedule schedule) {
        return null;
    }

    @Override
    public int update(Schedule schedule) {
        return 0;
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
