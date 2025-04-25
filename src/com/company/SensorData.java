
package com.company;
import java.time.LocalDateTime;
public class SensorData {
    private int spotID;
    private boolean isCarDetected;
    private LocalDateTime timestamp;

    public SensorData(int spotID, boolean isCarDetected, LocalDateTime timestamp) {
        this.spotID = spotID;
        this.isCarDetected = isCarDetected;
        this.timestamp = timestamp;
    }

    // Getters
    public int getSpotID() { return spotID; }
    public boolean isCarDetected() { return isCarDetected; }
}
