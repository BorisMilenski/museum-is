package com.borismilenski.museumis.dao;

import com.borismilenski.museumis.model.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionDAO {
    int addPosition(UUID id, Position position);
    default int addPosition(Position position){
        UUID id = UUID.randomUUID();
        return addPosition(id, position);
    }

    Optional<Position> getPosition(UUID id);
    List<Position> getAllPositions();
    int updatePosition(UUID id, Position newPosition);
    int deletePosition(UUID id);
}
