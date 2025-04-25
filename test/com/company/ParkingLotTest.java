package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    private ParkingLot parkingLot;
    private final int TEST_LOT_ID = 1;
    private final String TEST_LOCATION = "North Campus";

    @BeforeEach
    public void setUp() {
        parkingLot = new ParkingLot(TEST_LOT_ID, TEST_LOCATION);
    }

    @Test
    public void testParkingLotCreation() {
        // Verify parking lot was created correctly with initial values
        assertEquals(TEST_LOT_ID, parkingLot.getLotID());
        assertEquals(TEST_LOCATION, parkingLot.getLocation());
        assertEquals("enabled", parkingLot.getStatus());
    }



    @Test
    public void testSetStatus() {
        // Test updating the status
        String newStatus = "disabled";
        parkingLot.setStatus(newStatus);
        assertEquals(newStatus, parkingLot.getStatus());
    }
}