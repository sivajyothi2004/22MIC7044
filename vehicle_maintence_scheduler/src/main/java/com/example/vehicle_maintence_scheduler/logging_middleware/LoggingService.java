package com.example.vehicle_maintence_scheduler.logging_middleware;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class LoggingService {

    private static final String LOG_API =
            "http://4.224.186.213/evaluation-service/logs";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJzaXZhanlvdGhpLjIybWljNzA0NEB2aXRhcHN0dWRlbnQuYWMuaW4iLCJleHAiOjE3Nzg5MzExMTUsImlhdCI6MTc3ODkzMDIxNSwiaXNzIjoiQWZmb3JkIE1lZGljYWwgVGVjaG5vbG9naWVzIFByaXZhdGUgTGltaXRlZCIsImp0aSI6IjM2YWJiNzY5LTBlZWEtNGRiZC04YTgyLTFlY2RiM2RiOGVlNSIsImxvY2FsZSI6ImVuLUlOIiwibmFtZSI6InNpdmFqeW90aGkgYiIsInN1YiI6ImJmZmUyZDVjLTVmZjYtNDBkMi05NmQ0LTNmZDUxNTUyODE4YiJ9LCJlbWFpbCI6InNpdmFqeW90aGkuMjJtaWM3MDQ0QHZpdGFwc3R1ZGVudC5hYy5pbiIsIm5hbWUiOiJzaXZhanlvdGhpIGIiLCJyb2xsTm8iOiIyMm1pYzcwNDQiLCJhY2Nlc3NDb2RlIjoiU2ZGdVdnIiwiY2xpZW50SUQiOiJiZmZlMmQ1Yy01ZmY2LTQwZDItOTZkNC0zZmQ1MTU1MjgxOGIiLCJjbGllbnRTZWNyZXQiOiJOanR4WUhrQ0ZaVHpncFViIn0.pKsfS5HcsZfzj4gFJIQTJu9Q9e9N0QiL8bVhEVyPdhE";
    public static void Log(String stack,
                           String level,
                           String packageName,
                           String message) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(TOKEN);

            LogRequest request =
                    new LogRequest(
                            stack,
                            level,
                            packageName,
                            message
                    );

            HttpEntity<LogRequest> entity =
                    new HttpEntity<>(request, headers);

            restTemplate.exchange(
                    LOG_API,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {}
    }
}
