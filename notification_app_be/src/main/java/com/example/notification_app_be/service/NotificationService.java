package com.example.notification_app_be.service;

import com.example.notification_app_be.logging.LoggingService;
import com.example.notification_app_be.model.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {
    public List<Notification> getTopNotifications() {
        LoggingService.Log(
                "backend",
                "info",
                "service",
                "Fetching top notifications"
        );

        List<Notification> notifications = new ArrayList<>();

        notifications.add(
                new Notification(
                        "1",
                        "Placement",
                        "Amazon Hiring",
                        10
                )
        );
        notifications.add(
                new Notification(
                        "2",
                        "Event",
                        "Hackathon",
                        5
                )
        );

        notifications.add(
                new Notification(
                        "3",
                        "Result",
                        "Semester Results",
                        8
                )
        );

        notifications.sort(
                Comparator.comparingInt(
                        Notification::getPriority
                ).reversed()
        );

        return notifications.stream()
                .limit(10)
                .toList();
    }
}