package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.Schedule;
import com.borismilenski.museumis.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("api/schedule/test")
public class ScheduleController {
    private final LocalDate defaultStartingDay = LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), 1);
    private final LocalDate defaultEndingDay =  LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), 7);
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public Optional<Schedule> getSchedule(){
        //TODO: Add default scheduleRequests to the service
        int[][][] scheduleRequests = new int[][][]{
                {
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 1},
                        {0, 0},
                },
                {
                        {0, 0},
                        {0, 0},
                        {0, 1},
                        {0, 1},
                        {1, 0},
                        {0, 0},
                        {0, 0},
                },
                {
                        {0, 1},
                        {0, 1},
                        {0, 0},
                        {1, 0},
                        {0, 0},
                        {0, 1},
                        {0, 0},
                },
                {
                        {0, 0},
                        {0, 0},
                        {1, 0},
                        {0, 1},
                        {0, 0},
                        {1, 0},
                        {0, 0},
                }
        };
        return scheduleService.getSchedule(defaultStartingDay, defaultEndingDay, scheduleRequests);
    }
}
