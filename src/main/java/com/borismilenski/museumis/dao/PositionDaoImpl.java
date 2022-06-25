package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("positionDao")
public class PositionDaoImpl extends GenericDaoImpl<Position>{

    public PositionDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
    @Override
    String selectSingleInstanceSQL() {
        return  selectAllInstancesSQL() +
                " WHERE id_position = UUID_TO_BIN(?) ";
    }
    @Override
    String selectAllInstancesSQL() {
        return "" +
                "SELECT " +
                " BIN_TO_UUID(id_position), " +
                " base_pay, " +
                " days_off, " +
                " base_workload, " +
                " description, " +
                " name " +
                " FROM position ";
    }

    @Override
    public Position create(Position position){
        String sql = "" +
                "INSERT INTO position (" +
                " id_position, " +
                " base_pay, " +
                " days_off, " +
                " base_workload, " +
                " description, " +
                " name) " +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().update(
                sql,
                position.getId().toString(),
                position.getBasePay(),
                position.getDaysOff(),
                position.getWorkHoursPerWeek(),
                position.getDescription(),
                position.getName()
        );
        return position;
    }

    @Override
    public int update(Position position) {
        String sql = "" +
                "UPDATE position " +
                "SET " +
                "base_pay = ?, " +
                "days_off = ?, " +
                "base_workload = ?, " +
                "description = ?, " +
                "name  = ?" +
                "WHERE id_position = UUID_TO_BIN( ? )";
        return this.getJdbcTemplate().update(
                sql,
                position.getBasePay(),
                position.getDaysOff(),
                position.getWorkHoursPerWeek(),
                position.getDescription(),
                position.getName(),
                position.getId()
        );
    }

    @Override
    public RowMapper<Position> map() {
        return (resultSet, i) -> {
            String positionIdStr = resultSet.getString("BIN_TO_UUID(id_position)");
            UUID positionId = UUID.fromString(positionIdStr);

            String name = resultSet.getString("name");
            float basePay = resultSet.getFloat("base_pay");
            int daysOff = resultSet.getInt("days_off");
            int workHoursPerWeek = resultSet.getInt("base_workload");
            String description = resultSet.getString("description");

            return new Position(
                    positionId,
                    name,
                    basePay,
                    daysOff,
                    workHoursPerWeek,
                    description
            );
        };
    }


}
