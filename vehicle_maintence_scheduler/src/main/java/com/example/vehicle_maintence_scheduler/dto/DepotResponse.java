package com.example.vehicle_maintence_scheduler.dto;
import java.util.List;

public class DepotResponse {
    private List<Depot> depots;

    public List<Depot> getDepots() {
        return depots;
    }
    public void setDepots(List<Depot> depots) {
        this.depots = depots;
    }
}
