package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ManagerTest {

	private Manager manager;
	private ParkingLotManager plmanager;

	@BeforeEach
	void setUp() {
		manager = new Manager("Melissa", "mel@example.com", "!0!t25bi2Je1!");
		plmanager = new ParkingLotManager();
	}

	@Test
	void testDefaultConstructor() {
		Manager m = new Manager();
		assertEquals("", m.getName());
		assertEquals("", m.getEmail());
		assertEquals("", m.getPassword());
	}

	@Test
	void testConstructorWithParameters() {
		assertEquals("Melissa", manager.getName());
		assertEquals("mel@example.com", manager.getEmail());
		assertEquals("!0!t25bi2Je1!", manager.getPassword());
	}

	@Test
	void testAddParkingLotValid() {
		boolean result = manager.addParkingLot(plmanager, "Some Location");
		assertTrue(result);
	}

	@Test
	void testAddParkingLotInvalid() {
		boolean result = manager.addParkingLot(null, "Some Location");
		assertFalse(result);
	}

	// Reflected in CSV file.
	@Test
	void testEnableLotValidID() {
		manager.enableLot(plmanager, 1);
	}

	// Reflected in CSV file.
	@Test
	void testEnableLotInvalidID() {
		manager.enableLot(plmanager, 99);
	}

	@Test
	void testEnableLotNullManager() {
		assertDoesNotThrow(() -> manager.enableLot(null, 1));
	}

	// Reflected in CSV file.
	@Test
	void testEnableSpotValidID() {
		manager.enableSpot(plmanager, 1);
	}

	// Reflected in CSV file.
	@Test
	void testEnableSpotInvalidID() {
		manager.enableSpot(plmanager, 99);
	}

	// Reflected in CSV file.
	@Test
	void testDisableSpotValidID() {
		manager.disableSpot(plmanager, 1);
	}

	// Reflected in CSV file.
	@Test
	void testDisableSpotInvalidID() {
		manager.disableSpot(plmanager, 99);
	}

	@Test
	void testEnableSpotNullManager() {
		assertDoesNotThrow(() -> manager.enableSpot(null, 1));
	}

	@Test
	void testDisableSpotNullManager() {
		assertDoesNotThrow(() -> manager.disableSpot(null, 1));
	}

	// Reflected in CSV file.
	@Test
	void testDisableLotValidID() {
		manager.disableLot(plmanager, 1);
	}

	// Reflected in CSV file.
	@Test
	void testDisableLotInvalidID() {
		manager.disableLot(plmanager, 99);

	}

	@Test
	void testDisableLotNullManager() {
		assertDoesNotThrow(() -> manager.disableLot(null, 1));
	}

	@Test
	void testAlertAvailableSpace() {
		manager.alertAvailableSpace(101);
	}

	@Test
	void testAlertOverstay() {
		manager.alertOverstay(102);
	}

	@Test
	void testAlertMaintenance() {
		manager.alertMaintenance(103);
	}

	@Test
	void testAlertIllegalParking() {
		manager.alertIllegalParking(104);
	}

	@Test
	void testIsMaintenanceRequiredLotTrue() {
		boolean result = manager.isMaintenanceRequiredLot(4);
		assertTrue(result);
	}

	@Test
	void testIsMaintenanceRequiredLotFalse() {
		boolean result = manager.isMaintenanceRequiredLot(1);
		assertFalse(result);
	}

	@Test
	void testIsMaintenanceRequiredLotInvalidLot() {
		boolean result = manager.isMaintenanceRequiredLot(9999);
		assertFalse(result);
	}



	@Test
	void testIsMaintenanceRequiredSpotFalse() {
		boolean result = manager.isMaintenanceRequiredSpot(1);
		assertFalse(result);
	}

	@Test
	void testValidateYorkUValidUser() {
		String userType = manager.validateYorkU("mel@yorku.ca");
		assertNotNull(userType);
	}

	@Test
	void testValidateYorkUVisitor() {
		String userType = manager.validateYorkU("random@gmail.com");
		assertEquals("Visitor", userType);
	}

	@Test
	void testValidateYorkUNullEmail() {
		String userType = manager.validateYorkU(null);
		assertEquals("Visitor", userType);
	}

	@Test
	void testAddParkingLotExceptionHandling() {
		ParkingLotManager badPLM = new ParkingLotManager() {

			@Override
			public int getNextParkingLotID() {
				throw new RuntimeException("Fake IO Exception");
			}
		};


		boolean result = manager.addParkingLot(badPLM, "Fake Location");
		assertFalse(result);
	}


}

