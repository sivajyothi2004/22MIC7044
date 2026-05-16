CREATE DATABASE campus_notifications;

USE campus_notifications;

CREATE TABLE students (
    student_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE notifications (
    notification_id VARCHAR(50) PRIMARY KEY,
    student_id INT,
    title VARCHAR(255),
    message TEXT,
    notification_type VARCHAR(50),
    priority INT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(student_id)
    REFERENCES students(student_id)
);

CREATE INDEX idx_notification_query
ON notifications(student_id, is_read, created_at);