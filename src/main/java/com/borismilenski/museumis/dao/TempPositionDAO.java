package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("positionDAO")
public class TempPositionDAO implements PositionDAO{

    private final JdbcTemplate jdbcTemplate;

    public TempPositionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addPosition(UUID id, Position position) {
        String sql = "" +
                "INSERT INTO position (" +
                " UUID_TO_BIN(id_position), " +
                " base_pay, " +
                " days_off, " +
                " base_workload, " +
                " description " +
                " name) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                id,
                position.getBasePay(),
                position.getDaysOff(),
                position.getWorkHoursPerWeek(),
                position.getDescription(),
                position.getName()
        );
    }

    @Override
    public Optional<Position> getPosition(UUID id) {
        String sql = "" +
                "SELECT " +
                " BIN_TO_UUID(id_position), " +
                " base_pay, " +
                " days_off, " +
                " base_workload, " +
                " description " +
                " name " +
                " FROM position " +
                " WHERE BIN_TO_UUID(id_position) = ? "
                ;
        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                mapPositionFomDb()).stream().findFirst();
    }

    @Override
    public List<Position> getAllPositions() {
        String sql = "" +
                "SELECT " +
                " BIN_TO_UUID(id_position), " +
                " base_pay, " +
                " days_off, " +
                " base_workload, " +
                " description " +
                " name " +
                " FROM position " +
                " WHERE BIN_TO_UUID(id_position) = ? "
                ;
        return jdbcTemplate.query(
                sql,
                mapPositionFomDb());
    }

    @Override
    public int updatePosition(UUID id, Position newPosition) {
        return 0;
    }

    @Override
    public int deletePosition(UUID id) {
        return 0;
    }

    private RowMapper<Position> mapPositionFomDb() {
        return (resultSet, i) -> {
            String positionIdStr = resultSet.getString("BIN_TO_UUID(id_position)");
            UUID positionId = UUID.fromString(positionIdStr);

            String name = resultSet.getString("name");
            float basePay = resultSet.getFloat("base_pay");
            int daysOff = resultSet.getInt("daysOff");
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
