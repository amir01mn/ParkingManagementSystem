package com.company;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SensorDataTest {

    @Test
    public void testSensorDataConstructor() {
        // Setup
        int spotID = 123;
        boolean isCarDetected = true;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertEquals(spotID, sensorData.getSpotID());
        assertEquals(isCarDetected, sensorData.isCarDetected());
    }

    @Test
    public void testGetters() {
        // Setup
        int spotID = 456;
        boolean isCarDetected = false;
        LocalDateTime timestamp = LocalDateTime.of(2023, 12, 15, 14, 30);
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Act & Assert
        assertEquals(456, sensorData.getSpotID());
        assertFalse(sensorData.isCarDetected());
    }

    @Test
    public void testWithZeroSpotID() {
        // Setup
        int spotID = 0;
        boolean isCarDetected = true;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertEquals(0, sensorData.getSpotID());
    }

    @Test
    public void testWithNegativeSpotID() {
        // Setup
        int spotID = -10;
        boolean isCarDetected = false;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertEquals(-10, sensorData.getSpotID());
        // Note: In a real application, you might want to validate positive IDs,
        // but this test shows the current behavior
    }

    @Test
    public void testTrueCarDetection() {
        // Setup
        int spotID = 789;
        boolean isCarDetected = true;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertTrue(sensorData.isCarDetected());
    }

    @Test
    public void testFalseCarDetection() {
        // Setup
        int spotID = 101;
        boolean isCarDetected = false;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertFalse(sensorData.isCarDetected());
    }

    @Test
    public void testWithNullTimestamp() {
        // Setup
        int spotID = 202;
        boolean isCarDetected = true;

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, null);

    }

    @Test
    public void testWithPastTimestamp() {
        // Setup
        int spotID = 303;
        boolean isCarDetected = false;
        LocalDateTime pastTimestamp = LocalDateTime.of(2000, 1, 1, 0, 0);

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, pastTimestamp);

    }

    @Test
    public void testWithFutureTimestamp() {
        // Setup
        int spotID = 404;
        boolean isCarDetected = true;
        LocalDateTime futureTimestamp = LocalDateTime.now().plusYears(1);

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, futureTimestamp);

    }

    @Test
    public void testMaxIntegerSpotID() {
        // Setup
        int spotID = Integer.MAX_VALUE;
        boolean isCarDetected = true;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        SensorData sensorData = new SensorData(spotID, isCarDetected, timestamp);

        // Assert
        assertEquals(Integer.MAX_VALUE, sensorData.getSpotID());
    }
}