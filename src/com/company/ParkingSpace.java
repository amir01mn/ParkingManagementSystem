package com.company;

public class ParkingSpace {
    private int spotID;
    private int lotID;
    private String status;
    private int sensorID;
    private String needMaintenance;


    public ParkingSpace(int spotID, int lotID, int sensorID) {
        this.spotID = spotID;
        this.lotID = lotID;
        this.sensorID = sensorID;
        this.status = "Available";
        this.needMaintenance = "FALSE";
    }

    public void setStatus(String status) {
        this.status = status;

    }
    public void setNeedMaintenance(String needMaintenance) {
        this.needMaintenance = needMaintenance;
    }
    public String getNeedMaintenance() {
        return this.needMaintenance;
    }


    // Check if the space is currently available
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(status);
    }



    public void updateFromSensor(boolean isOccupied) {
        setStatus(isOccupied ? "Occupied" : "Available");
    }

    public int getSpotID() {
        return spotID;
    }

    public int getLotID() {
        return lotID;
    }

    public String getStatus() {
        return status;
    }

    public int getSensorID() {
        return sensorID;
    }
}
