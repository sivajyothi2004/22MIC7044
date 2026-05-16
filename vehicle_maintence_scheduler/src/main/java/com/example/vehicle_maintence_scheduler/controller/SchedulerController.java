package com.example.vehicle_maintence_scheduler.controller;

import com.example.vehicle_maintence_scheduler.dto.ScheduleResult;
import com.example.vehicle_maintence_scheduler.service.SchedulerService;
import com.example.vehicle_maintence_scheduler.logging_middleware.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/schedule")
    public List<ScheduleResult> getSchedules() {

        LoggingService.Log(
                "backend",
                "info",
                "controller",
                "Schedule API called"
        );
        return schedulerService.generateSchedules();
    }
}
