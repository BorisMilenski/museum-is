package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.Schedule;
import com.borismilenski.museumis.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {
    private final LocalDate defaultStartingDay = LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), 1);
    private final LocalDate defaultEndingDay =  LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), 7);
    private final int[][][] scheduleRequests = new int[][][]{};
    private final ScheduleService scheduleService;
    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    @GetMapping
    public Optional<Schedule> getSchedule(){
        return scheduleService.getSchedule(defaultStartingDay, defaultEndingDay, scheduleRequests);
    }
    @GetMapping(path = "{webNiceName}")
    public Optional<Schedule> getScheduleForEmployee(@PathVariable("webNiceName") String webNiceName){
        return scheduleService.getScheduleForEmployee(webNiceName, defaultStartingDay, defaultEndingDay, scheduleRequests);
    }

}
