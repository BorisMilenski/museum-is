package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.GenericDaoImpl;
import com.borismilenski.museumis.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PositionService extends GenericService<Position>{
    @Autowired
    protected PositionService(@Qualifier("positionDao") GenericDaoImpl<Position> genericDao) {
        super(genericDao);
    }
}
