package com.example.vehicle_maintence_scheduler.dto;
import java.util.List;

public class ScheduleResult {

    private int depotId;
    private List<String> selectedTasks;
    private int totalImpact;

    public ScheduleResult(int depotId, List<String> selectedTasks, int totalImpact) {
        this.depotId = depotId;
        this.selectedTasks = selectedTasks;
        this.totalImpact = totalImpact;
    }

    public int getDepotId() {
        return depotId;
    }

    public List<String> getSelectedTasks() {
        return selectedTasks;
    }

    public int getTotalImpact() {
        return totalImpact;
    }
}