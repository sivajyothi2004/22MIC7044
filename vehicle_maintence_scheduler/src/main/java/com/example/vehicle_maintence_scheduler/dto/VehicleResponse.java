package com.example.vehicle_maintence_scheduler.dto;

import java.util.List;
public class VehicleResponse {

    private List<VehicleTask> vehicles;

    public List<VehicleTask> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleTask> vehicles) {
        this.vehicles = vehicles;
    }
}