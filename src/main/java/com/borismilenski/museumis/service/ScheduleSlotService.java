package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.SlotDao;
import com.borismilenski.museumis.model.ScheduleSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleSlotService extends GenericService<ScheduleSlot>{
    @Autowired
    protected ScheduleSlotService(@Qualifier("slotDao") SlotDao slotDao) {
        super(slotDao);
    }

    public List<ScheduleSlot> findAllforEmployee(UUID id){
        return ((SlotDao) this.getGenericDao()).findAllforEmployee(id);
    }
}
