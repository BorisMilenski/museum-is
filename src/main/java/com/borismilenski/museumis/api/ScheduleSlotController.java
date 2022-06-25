package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.ScheduleSlot;
import com.borismilenski.museumis.service.ScheduleSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/slots")
public class ScheduleSlotController {
    private final ScheduleSlotService scheduleSlotService;

    @Autowired
    public ScheduleSlotController(ScheduleSlotService scheduleSlotService) {
        this.scheduleSlotService = scheduleSlotService;
    }

    @GetMapping
    public List<ScheduleSlot> getScheduleSlots(){
        return scheduleSlotService.findAll();
    }

    @GetMapping(path = "{employeeId}")
    public List<ScheduleSlot> getScheduleSlot(@PathVariable("employeeId") UUID employeeId){
        return scheduleSlotService.findAllforEmployee(employeeId);
    }
}
