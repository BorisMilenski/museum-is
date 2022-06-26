package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.util.DBToClassMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericDaoImpl<T> implements GenericDao<T>, DBToClassMapper<T> {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public GenericDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<T> find(UUID id) {
        String selectQuery =  selectSingleInstanceSQL();
        return jdbcTemplate.query(
                selectQuery,
                new Object[]{id.toString()},
                this.map()
        ).stream().findFirst();
    }

    @Override
    public List<T> findAll() {
            String selectQuery = selectAllInstancesSQL();
            return jdbcTemplate.query(
                    selectQuery,
                    this.map());
    }

    @Override
    public void delete(UUID id){
        String sql = "" +
                "DELETE FROM student " +
                "WHERE student_id = ?";
        jdbcTemplate.update(sql, id); //TODO: Do something with the return value of template.update (rows affected)
    }

    abstract String selectSingleInstanceSQL();
    abstract String selectAllInstancesSQL();
}
