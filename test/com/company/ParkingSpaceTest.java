package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ParkingSpaceTest {

	private ParkingSpace parkingspace;

	@BeforeEach
    void setUp() {
        parkingspace = new ParkingSpace(1, 5, 10);
    }

	@Test
	void testStatusIsAvailableInitially() {
		assertEquals("Available", parkingspace.getStatus());
	}

	@Test
	void testIsAvailableInitiallyTrue() {
		assertTrue(parkingspace.isAvailable());
	}

	@Test
	void testSetStatusOccupied() {
		parkingspace.setStatus("Occupied");
		assertEquals("Occupied", parkingspace.getStatus());
	}

	@Test
	void testSetStatusDisabled() {
		parkingspace.setStatus("Disabled");
		assertEquals("Disabled", parkingspace.getStatus());
		assertFalse(parkingspace.isAvailable());
	}

	@Test
	void testSetStatusNull() {
		parkingspace.setStatus(null);
		assertFalse(parkingspace.isAvailable());
	}

	@Test
	void checkAvailabilityAfterSettingOccupied() {
		parkingspace.setStatus("Occupied");
		assertFalse(parkingspace.isAvailable());
	}

	@Test
	void checkUpdateFromSensorWhenAvailable() {
		parkingspace.updateFromSensor(false);
		assertEquals("Available", parkingspace.getStatus());
		assertTrue(parkingspace.isAvailable());
	}

	@Test
	void checkUpdateFromSensorWhenOccupied() {
		parkingspace.updateFromSensor(true);
		assertEquals("Occupied", parkingspace.getStatus());
		assertFalse(parkingspace.isAvailable());
	}

	@Test
	void testGetSpotID() {
		assertEquals(1, parkingspace.getSpotID());
	}

	@Test
	void testGetSLotID() {
		assertEquals(5, parkingspace.getLotID());
	}
	@Test
	void testGetSensorID() {
		assertEquals(10, parkingspace.getSensorID());
	}

}