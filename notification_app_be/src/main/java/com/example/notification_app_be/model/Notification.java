package com.example.notification_app_be.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class Notification {

    private String id;
    private String type;
    private String message;
    private int priority;
}