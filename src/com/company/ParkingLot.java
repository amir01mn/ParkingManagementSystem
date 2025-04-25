package com.company;
import java.util.ArrayList;
import java.util.List;


public class ParkingLot {
    private int lotID;
    private String location;
    private List<ParkingSpace> spaces;
    private String status;
    private String needMaintenance;

    public ParkingLot(int lotID, String location) {
        this.lotID = lotID;
        this.location = location;
        this.spaces = new ArrayList<>();
        this.status = "enabled";
        this.needMaintenance = "FALSE";// Default status

    }
    public void setNeedMaintenance(String needMaintenance) {
        this.needMaintenance = needMaintenance;
    }
    public String getNeedMaintenance() {
        return this.needMaintenance;
    }

    // Getters and Setters
    public int getLotID() {
        return lotID;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

    }
}
