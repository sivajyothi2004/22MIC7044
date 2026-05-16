
# Campus Notification Microservice Design

# Stage 1

## REST API Design

### 1. Create Notification

Endpoint:

```http
POST /api/notifications
```

Request Body:

```json
{
  "title": "Amazon Placement Drive",
  "message": "Amazon hiring for SDE roles",
  "type": "Placement",
  "priority": 10
}
```

Response:

```json
{
  "message": "Notification created successfully"
}
```

---

### 2. Get Notifications

Endpoint:

```http
GET /api/notifications
```

Response:

```json
[
  {
    "id": "1",
    "type": "Placement",
    "message": "Amazon Hiring",
    "priority": 10
  }
]
```

---

### 3. Mark Notification as Read

Endpoint:

```http
PUT /api/notifications/{id}
```

Response:

```json
{
  "message": "Notification marked as read"
}
```

---

### 4. Delete Notification

Endpoint:

```http
DELETE /api/notifications/{id}
```

Response:

```json
{
  "message": "Notification deleted"
}
```

---

## Real-Time Notification Mechanism

Recommended approaches:

- WebSockets
- Server-Sent Events (SSE)

Advantages:

- Real-time updates
- Low latency
- Better user experience

---

# Stage 2

## Database Choice

Database Used:

```text
MySQL
```

Reason:

- Relational structure
- Easy indexing
- Strong query support
- Good scalability

---

## MySQL Schema

### Students Table

```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

---

### Notifications Table

```sql
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
```

---

## Problems with Large Data

Potential issues:

- slow queries
- large table scans
- high DB load

---

## Solutions

### Indexing

```sql
CREATE INDEX idx_notification_query
ON notifications(student_id, is_read, created_at);
```

### Caching

Use Redis for:
- unread notifications
- recent notifications

### Partitioning

Partition data by:
- month
- year

---

# Stage 3

## Existing Query

```sql
SELECT *
FROM notifications
WHERE student_id = 1042
AND is_read = false
ORDER BY created_at DESC;
```

---

## Is It Accurate?

Yes.

It correctly fetches unread notifications.

---

## Why Could It Become Slow?

- millions of rows
- missing indexes
- sorting overhead

---

## Optimization

### Composite Index

```sql
CREATE INDEX idx_notification_query
ON notifications(student_id, is_read, created_at);
```

---

## Why Not Add Indexes Everywhere?

Too many indexes:
- slow inserts
- slow updates
- consume storage

Indexes should only be added on:
- frequently queried columns
- sorting columns
- join columns

---

## Placement Notifications Query

```sql
SELECT DISTINCT student_id
FROM notifications
WHERE notification_type = 'Placement'
AND created_at >= NOW() - INTERVAL 7 DAY;
```

---

# Stage 4

## Problem

Fetching notifications from DB on every request can:
- overload DB
- slow responses

---

## Solutions

### Redis Caching

Store:
- unread counts
- latest notifications

Benefits:
- very fast retrieval
- reduced DB load

---

### Pagination

Load only:
- latest 20 notifications

Benefits:
- faster response
- reduced payload size

---

### WebSockets

Push notifications instantly.

Benefits:
- real-time communication
- avoids repeated polling

---

### Read Replicas

Use separate DB replicas for read traffic.

Benefits:
- distributes DB load

---

# Stage 5

## Existing Implementation Problems

```python
function notify_all(student_ids, message):
    for student_id in student_ids:
        send_email(student_id, message)
        save_to_db(student_id, message)
        push_to_app(student_id, message)
```

Problems:
- sequential execution
- slow processing
- no retry mechanism
- difficult scalability

---

## Better Architecture

Recommended:
- Kafka / RabbitMQ
- async workers
- retry queues
- dead letter queues

---

## Improved Flow

1. Save notification event
2. Push event to message queue
3. Workers process notifications independently

Advantages:
- scalable
- fault tolerant
- faster delivery

---

# Stage 6

## Priority Inbox System

Priority logic:
- Placement → highest priority
- Result → medium priority
- Event → low priority

Also consider:
- recent notifications first

---

## Efficient Data Structure

Use:
- Priority Queue (Max Heap)

Benefits:
- O(log n) insertion
- efficient top notification retrieval

---

## Java Implementation

Implemented inside:

```text
notification_app_be
```

Features:
- sorting notifications
- retrieving top notifications
- Spring Boot REST API
- logging middleware integration

---

# Technologies Used

- Spring Boot
- Java
- MySQL
- REST APIs
- Logging Middleware
