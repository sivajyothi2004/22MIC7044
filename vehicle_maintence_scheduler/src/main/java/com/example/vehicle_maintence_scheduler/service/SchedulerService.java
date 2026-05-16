package com.example.vehicle_maintence_scheduler.service;

import com.example.vehicle_maintence_scheduler.dto.*;
import com.example.vehicle_maintence_scheduler.logging_middleware.LoggingService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchedulerService {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJzaXZhanlvdGhpLjIybWljNzA0NEB2aXRhcHN0dWRlbnQuYWMuaW4iLCJleHAiOjE3Nzg5MzExMTUsImlhdCI6MTc3ODkzMDIxNSwiaXNzIjoiQWZmb3JkIE1lZGljYWwgVGVjaG5vbG9naWVzIFByaXZhdGUgTGltaXRlZCIsImp0aSI6IjM2YWJiNzY5LTBlZWEtNGRiZC04YTgyLTFlY2RiM2RiOGVlNSIsImxvY2FsZSI6ImVuLUlOIiwibmFtZSI6InNpdmFqeW90aGkgYiIsInN1YiI6ImJmZmUyZDVjLTVmZjYtNDBkMi05NmQ0LTNmZDUxNTUyODE4YiJ9LCJlbWFpbCI6InNpdmFqeW90aGkuMjJtaWM3MDQ0QHZpdGFwc3R1ZGVudC5hYy5pbiIsIm5hbWUiOiJzaXZhanlvdGhpIGIiLCJyb2xsTm8iOiIyMm1pYzcwNDQiLCJhY2Nlc3NDb2RlIjoiU2ZGdVdnIiwiY2xpZW50SUQiOiJiZmZlMmQ1Yy01ZmY2LTQwZDItOTZkNC0zZmQ1MTU1MjgxOGIiLCJjbGllbnRTZWNyZXQiOiJOanR4WUhrQ0ZaVHpncFViIn0.pKsfS5HcsZfzj4gFJIQTJu9Q9e9N0QiL8bVhEVyPdhE";

    private static final String DEPOT_API =
            "http://4.224.186.213/evaluation-service/depots";

    private static final String VEHICLE_API =
            "http://4.224.186.213/evaluation-service/vehicles";

    public List<ScheduleResult> generateSchedules() {

        LoggingService.Log(
                "backend",
                "info",
                "service",
                "Schedule generation started"
        );

        List<ScheduleResult> results = new ArrayList<>();

        try {

            List<Depot> depots = fetchDepots();

            List<VehicleTask> vehicles = fetchVehicles();

            for (Depot depot : depots) {

                ScheduleResult result = optimizeTasks(
                        depot.getID(),
                        depot.getMechanicHours(),
                        vehicles
                );

                results.add(result);
            }

            LoggingService.Log(
                    "backend",
                    "info",
                    "service",
                    "Schedule generation completed"
            );

        } catch (Exception e) {

            LoggingService.Log(
                    "backend",
                    "error",
                    "handler",
                    e.getMessage()
            );
        }

        return results;
    }

    private List<Depot> fetchDepots() {

        LoggingService.Log(
                "backend",
                "debug",
                "service",
                "Fetching depots"
        );

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(TOKEN);

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<DepotResponse> response =
                restTemplate.exchange(
                        DEPOT_API,
                        HttpMethod.GET,
                        entity,
                        DepotResponse.class
                );

        return response.getBody().getDepots();
    }

    private List<VehicleTask> fetchVehicles() {

        LoggingService.Log(
                "backend",
                "debug",
                "service",
                "Fetching vehicle tasks"
        );

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(TOKEN);

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<VehicleResponse> response =
                restTemplate.exchange(
                        VEHICLE_API,
                        HttpMethod.GET,
                        entity,
                        VehicleResponse.class
                );

        return response.getBody().getVehicles();
    }

    private ScheduleResult optimizeTasks(int depotId,
                                         int capacity,
                                         List<VehicleTask> tasks) {

        LoggingService.Log(
                "backend",
                "debug",
                "service",
                "Running optimization"
        );

        int n = tasks.size();
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            VehicleTask task = tasks.get(i - 1);
            for (int w = 0; w <= capacity; w++) {

                if (task.getDuration() <= w) {

                    dp[i][w] = Math.max(
                            task.getImpact()
                                    + dp[i - 1][w - task.getDuration()],
                            dp[i - 1][w]
                    );

                } else {

                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<String> selectedTasks = new ArrayList<>();
        int w = capacity;

        for (int i = n; i > 0; i--) {

            if (dp[i][w] != dp[i - 1][w]) {
                VehicleTask task = tasks.get(i - 1);
                selectedTasks.add(task.getTaskID());
                w -= task.getDuration();
            }
        }

        Collections.reverse(selectedTasks);

        return new ScheduleResult(
                depotId,
                selectedTasks,
                dp[n][capacity]
        );
    }
}