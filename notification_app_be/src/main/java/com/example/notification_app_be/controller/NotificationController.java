package com.example.notification_app_be.controller;

import com.example.notification_app_be.logging.LoggingService;
import com.example.notification_app_be.model.Notification;
import com.example.notification_app_be.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/api/priority")
    public List<Notification> getPriorityNotifications() {

        LoggingService.Log(
                "backend",
                "info",
                "controller",
                "Priority notifications API called"
        );

        return notificationService.getTopNotifications();
    }
}