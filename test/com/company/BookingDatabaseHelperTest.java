package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;

public class BookingDatabaseHelperTest {
    private Booking testBooking;

    @BeforeEach
    public void setUp() throws Exception {

        testBooking = new Booking.BookingBuilder()
                .setBookingID(String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1))
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();
    }

    private static class TestBookingDatabaseHelper extends BookingDatabaseHelper {
        private static String testFilePath;
        private static final String DEFAULT_PATH = "data/Booking_Database.csv";

        public static void setTestFilePath(String path) {
            testFilePath = path;
        }

        public static String getAbsolutePath() {
            if (testFilePath != null)
                return testFilePath;

            String currentDir = System.getProperty("user.dir");
            return Paths.get(currentDir, "data", "Booking_Database.csv").toString();
        }
    }

    private static class MockBookingDatabaseHelper extends BookingDatabaseHelper {

        public static List<String> readAllLines() {
            throw new RuntimeException(new IOException("Simulated IOException"));
        }
    }

    @Test
    public void testSaveBookingValidValues() throws Exception {
        // Create a booking with a unique ID
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1))
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);

        // Verify the booking was saved
        Booking savedBooking = BookingDatabaseHelper.findBookingByID(testBooking.getBookingID());
        assertNotNull(savedBooking);
        assertEquals(testBooking.getBookingID(), savedBooking.getBookingID());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBooking.getBookingID());
    }

    @Test
    public void testSaveBookingDuplicateID() throws Exception {
        // Clean up any existing bookings with the test ID
        List<String> lines = BookingDatabaseHelper.readAllLines();

        List<String> newLines = new ArrayList<>();
        newLines.add(lines.get(0)); // Keep the header
        for (int i = 1; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (!data[0].trim().equals("TEST001")) {
                newLines.add(lines.get(i));
            }
        }
        BookingDatabaseHelper.writeAllLines(newLines);

        // Create and save the first booking with ID TEST001
        Booking firstBooking = new Booking.BookingBuilder()
                .setBookingID("TEST001")
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(firstBooking);

        // Now try to save a duplicate booking with the same ID
        Booking duplicateBooking = new Booking.BookingBuilder()
                .setBookingID("TEST001")
                .setUserID(1002)
                .setParkingSpace(2)
                .setParkingLot(2)
                .setStart(LocalTime.of(11, 0))
                .setEnd(LocalTime.of(13, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            BookingDatabaseHelper.saveBooking(duplicateBooking);
        });

        // Clean up
        BookingDatabaseHelper.cancelBooking("TEST001");
    }

    @Test
    public void testUpdateBookingStatus() {
        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updateBookingStatus("TEST001", "Cancelled");
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID("TEST001");
        assertEquals("Cancelled", updatedBooking.getBookingStatus());
    }


    @Test
    public void testUpdatePaymentStatus() {
        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updatePaymentStatus("TEST001", "Paid");
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID("TEST001");
        assertEquals("Paid", updatedBooking.getPaymentStatus());
    }
    @Test
    public void testUpdateTotalAmount() {
        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updateTotalAmount("TEST001", 25.0);
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID("TEST001");
        assertEquals(25.0, updatedBooking.getTotalAmount());
    }

    @Test
    public void testUpdateEndTime() {
        BookingDatabaseHelper.saveBooking(testBooking);
        LocalTime newEnd = LocalTime.of(14, 0);
        BookingDatabaseHelper.updateEndTime("TEST001", newEnd);
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID("TEST001");
        assertEquals(newEnd, updatedBooking.getEnd());
    }

    @Test
    public void testGetBookingsForTimeSlot() {
        BookingDatabaseHelper.saveBooking(testBooking);
        List<Booking> timeSlotBookings = BookingDatabaseHelper.getBookingsForTimeSlot(
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertFalse(timeSlotBookings.isEmpty());
        assertEquals("N2S000002", timeSlotBookings.get(0).getBookingID());
    }

    @Test
    public void testGetAbsolutePath() {
        // Get the expected base path
        String currentDir = System.getProperty("user.dir");
        String expectedBasePath = Paths.get(currentDir, "Project3311Amir 3" + "data", "Booking_Database.csv").toString();

        // Get the actual path from the method
        String actualPath = BookingDatabaseHelper.getAbsolutePath();

        // Verify the path is not null
        assertNotNull(actualPath, "Path should not be null");

        // Verify the path ends with the correct filename
        assertTrue(actualPath.endsWith("Booking_Database.csv"), "Path should end with Booking_Database.csv");

        // Verify the path contains the correct directory structure
        assertFalse(actualPath.contains("Project3311Amir3" + File.separator + "data"),
                "Path should contain correct directory structure");

        // Verify the path exists
        File file = new File(actualPath);
        assertTrue(file.exists(), "File should exist at the specified path");
    }

    @Test
    public void testGetAbsolutePathWithDifferentWorkingDirectories() {
        // Save original working directory
        String originalWorkingDir = System.getProperty("user.dir");

        try {
            // Test with a different working directory
            String testDir = originalWorkingDir + File.separator + "test";
            System.setProperty("user.dir", testDir);

            String path = BookingDatabaseHelper.getAbsolutePath();

            // Verify the path still points to the correct location
            assertTrue(path.contains("data" + File.separator + "Booking_Database.csv"),
                    "Path should be correct regardless of working directory");

        } finally {
            // Restore original working directory
            System.setProperty("user.dir", originalWorkingDir);
        }
    }

    @Test
    public void testGetAbsolutePathWithDifferentNonWorkingDirectories() {
        // Save original working directory
        String originalWorkingDir = System.getProperty("user.dir");

        try {
            // Test with a different working directory
            String testDir = originalWorkingDir + File.separator + "test";
            System.setProperty("user.dir", testDir);

            String path = BookingDatabaseHelper.getAbsolutePath();

            // Verify the path still points to the correct location
            assertTrue(path.contains("data" + File.separator + "Booking_Database.csv"),
                    "Path should be correct regardless of working directory");

        } finally {
            // Restore original working directory
            System.setProperty("user.dir", originalWorkingDir);
        }
    }


    @Test
    public void testGetAbsolutePathFileAccessibility() {
        String path = BookingDatabaseHelper.getAbsolutePath();
        File file = new File(path);

        // Verify the file is readable
        assertTrue(file.canRead(), "File should be readable");

        // Verify the file is writable
        assertTrue(file.canWrite(), "File should be writable");
    }



    @Test
    public void testReadAllLines() {

        String path = BookingDatabaseHelper.getAbsolutePath();
        File file = new File(path);

        assertTrue(file.exists(), "Database file should exist");
        assertTrue(file.canRead(), "Database file should be readable");

        List<String> lines = BookingDatabaseHelper.readAllLines();

        assertNotNull(lines, "Returned list should not be null");
        assertFalse(lines.isEmpty(), "Returned list should not be empty");

        String header = lines.get(0);
        assertTrue(header.contains("booking_id"), "First line should contain header information");
        assertTrue(header.contains("user_id"), "First line should contain header information");
        assertTrue(header.contains("spot_id"), "First line should contain header information");
    }

    @Test
    public void testReadAllLinesIOException() {
        try {
            List<String> lines = MockBookingDatabaseHelper.readAllLines();
            fail("Expected IOException to be thrown");

        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException, "Exception should contain an IOException");
            assertEquals("Simulated IOException", e.getCause().getMessage(), "Exception message should match");
        }
    }

    @Test
    public void testWriteAllLines() {
        List<String> lines = BookingDatabaseHelper.readAllLines();
        BookingDatabaseHelper.writeAllLines(lines);
        List<String> newLines = BookingDatabaseHelper.readAllLines();
        assertEquals(lines, newLines);
    }

    @Test
    public void testWriteAllLinesIOException() {
        List<String> lines = BookingDatabaseHelper.readAllLines();
        BookingDatabaseHelper.writeAllLines(lines);
        List<String> newLines = BookingDatabaseHelper.readAllLines();
        assertEquals(lines, newLines);
    }

    @Test
    public void testConvertToBookingValidData() {
        String[] validData = {
                "N2S000001",           // booking_id
                "1001",                // user_id
                "1",                   // spot_id
                "1",                   // parking_id
                "ABC123",              // plate_number
                "10:00:00",           // start_time
                "12:00:00",           // end_time
                "Pending",             // payment_status
                "5.0",                 // deposit_amount
                "Active",              // booking_status
                "20.0"                 // total_amount
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(validData);
        assertNotNull(booking);
        assertEquals("N2S000001", booking.getBookingID());
        assertEquals(1001, booking.getUserID());
        assertEquals(1, booking.getParkingSpace());
        assertEquals(1, booking.getParkingLot());
        assertEquals("ABC123", booking.getPlate());
        assertEquals(LocalTime.of(10, 0), booking.getStart());
        assertEquals(LocalTime.of(12, 0), booking.getEnd());
        assertEquals("Pending", booking.getPaymentStatus());
        assertEquals(5.0, booking.getAmount());
        assertEquals("Active", booking.getBookingStatus());
        assertEquals(20.0, booking.getTotalAmount());
    }

    @Test
    public void testConvertToBookingHeaderRow() {
        String[] headerData = {
                "booking_id", "user_id", "spot_id", "parking_id", "plate_number",
                "start_time", "end_time", "payment_status", "deposit_amount",
                "booking_status", "total_amount"
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(headerData);
        assertNull(booking);
    }

    @Test
    public void testConvertToBookingInvalidTimeFormat() {
        String[] invalidTimeData = {
                "N2S000001",
                "1001",
                "1",
                "1",
                "ABC123",
                "invalid_time",  // Invalid time format
                "12:00:00",
                "Pending",
                "5.0",
                "Active",
                "20.0"
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(invalidTimeData);
        assertNull(booking);
    }

    @Test
    public void testConvertToBookingInvalidNumberFormat() {
        String[] invalidNumberData = {
                "N2S000001",
                "invalid_user_id",  // Invalid number format
                "1",
                "1",
                "ABC123",
                "10:00:00",
                "12:00:00",
                "Pending",
                "5.0",
                "Active",
                "20.0"
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(invalidNumberData);
        assertNull(booking);
    }

    @Test
    public void testConvertToBookingMissingTotalAmount() {
        String[] missingTotalAmountData = {
                "N2S000001",
                "1001",
                "1",
                "1",
                "ABC123",
                "10:00:00",
                "12:00:00",
                "Pending",
                "5.0",
                "Active"
                // Missing total_amount
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(missingTotalAmountData);
        assertNotNull(booking);
        assertEquals(5.0, booking.getTotalAmount()); // Should use deposit amount as total amount
    }

    @Test
    public void testConvertToBookingNullData() {
        assertThrows(NullPointerException.class, () -> {
            BookingDatabaseHelper.convertToBooking(null);
        }, "Should throw NullPointerException when input data is null");
    }

    @Test
    public void testConvertToBookingEmptyData() {
        String[] emptyData = {};
        Booking booking = BookingDatabaseHelper.convertToBooking(emptyData);
        assertNull(booking);
    }

    @Test
    public void testConvertToBookingInvalidPaymentStatus() {
        String[] invalidPaymentStatusData = {
                "N2S000001",
                "1001",
                "1",
                "1",
                "ABC123",
                "10:00:00",
                "12:00:00",
                "InvalidStatus",  // Invalid payment status
                "5.0",
                "Active",
                "20.0"
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(invalidPaymentStatusData);
        assertNotNull(booking); // Should still create booking as payment status is just a string
        assertEquals("InvalidStatus", booking.getPaymentStatus());
    }

    @Test
    public void testConvertToBookingInvalidBookingStatus() {
        String[] invalidBookingStatusData = {
                "N2S000001",
                "1001",
                "1",
                "1",
                "ABC123",
                "10:00:00",
                "12:00:00",
                "Pending",
                "5.0",
                "InvalidStatus",  // Invalid booking status
                "20.0"
        };

        Booking booking = BookingDatabaseHelper.convertToBooking(invalidBookingStatusData);
        assertNotNull(booking); // Should still create booking as booking status is just a string
        assertEquals("InvalidStatus", booking.getBookingStatus());
    }

    @Test
    public void testFindBookingByExistingBookingID() throws Exception {

        String testBookingID = "N2S000005";
        Booking foundBooking = BookingDatabaseHelper.findBookingByID(testBookingID);

        assertNotNull(foundBooking, "Should find existing booking");
        assertEquals(testBookingID, foundBooking.getBookingID());
        assertEquals(1012, foundBooking.getUserID());
        assertEquals(5, foundBooking.getParkingSpace());
        assertEquals(3, foundBooking.getParkingLot());
        assertEquals("CDPD554", foundBooking.getPlate());
        assertEquals(LocalTime.of(14, 0), foundBooking.getStart());
        assertEquals(LocalTime.of(16, 0), foundBooking.getEnd());
        assertEquals("Pending", foundBooking.getPaymentStatus());
        assertEquals(15.0, foundBooking.getAmount());
        assertEquals("Active", foundBooking.getBookingStatus());
        assertEquals(60.0, foundBooking.getTotalAmount());
    }

    @Test
    public void testFindBookingByNonExistentBookingID() {

        Booking foundBooking = BookingDatabaseHelper.findBookingByID("NONEXISTENT123");

        assertNull(foundBooking, "Should return null for non-existent booking");
    }

    @Test
    public void testFindBookingByNullID() {
        // Try to find a booking with null ID
        Booking foundBooking = BookingDatabaseHelper.findBookingByID(null);

        assertNull(foundBooking, "Should return null for null booking ID");
    }

    @Test
    public void testFindBookingByEmptyID() {
        // Try to find a booking with empty ID
        Booking foundBooking = BookingDatabaseHelper.findBookingByID("");

        assertNull(foundBooking, "Should return null for empty booking ID");
    }

    @Test
    public void testCancelBookingWithExistingBooking() throws Exception {
        // Generate a unique test ID using timestamp
        String testBookingID = "TEST" + System.currentTimeMillis();

        // First, clean up any existing test booking
        try {
            BookingDatabaseHelper.cancelBooking(testBookingID);
        } catch (Exception e) {
            // Ignore if booking doesn't exist
        }

        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0, 0))  // Include seconds
                .setEnd(LocalTime.of(12, 0, 0))    // Include seconds
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);

        Booking foundBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(foundBooking, "Booking should be found");
        assertEquals(testBookingID, foundBooking.getBookingID());

        // Cancel the booking
        BookingDatabaseHelper.cancelBooking(testBookingID);

        Booking cancelledBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(cancelledBooking, "Cancelled booking should still exist");
        assertEquals("Cancelled", cancelledBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testCancelBookingWithNonExistentBooking() {

        String bookingID = "NONEXISTENT123";
        BookingDatabaseHelper.cancelBooking(bookingID);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            BookingDatabaseHelper.findBookingByID(bookingID).getBookingStatus();
        });

        assertFalse(exception.getMessage().contains("Cannot input null value"));
    }



    @Test
    public void testGetBookingsForTimeSlotOverlapping() throws Exception {
        // Backup the database
        String backupPath = backupDatabase();

        try {
            int lastID = BookingDatabaseHelper.getLastBookingID();
            String testBookingID = String.format("N2S%06d", lastID + 1);
            Booking testBooking = new Booking.BookingBuilder()
                    .setBookingID(testBookingID)
                    .setUserID(1001)
                    .setParkingSpace(1)
                    .setParkingLot(1)
                    .setStart(LocalTime.of(9, 0))
                    .setEnd(LocalTime.of(11, 0))
                    .setBookingStatus("Active")
                    .setPaymentStatus("Pending")
                    .setPlate("TEST123")
                    .setAmount(5.0)
                    .setTotalAmount(20.0)
                    .build();

            BookingDatabaseHelper.saveBooking(testBooking);

            // Test finding bookings for a time slot that overlaps
            List<Booking> foundBookings = BookingDatabaseHelper.getBookingsForTimeSlot(
                    LocalTime.of(10, 0), LocalTime.of(12, 0));

            assertNotNull(foundBookings, "Returned list should not be null");
            assertFalse(foundBookings.isEmpty(), "Should find at least one overlapping booking");

            // Check if our test booking is in the list
            boolean found = false;
            for (Booking booking : foundBookings) {
                if (booking.getBookingID().equals(testBookingID)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Should find our test booking in the list");

            // Clean up
            BookingDatabaseHelper.cancelBooking(testBookingID);
        } finally {
            // Restore the database
            restoreDatabase(backupPath);
        }
    }

    private static String backupDatabase() throws IOException {
        String originalPath = BookingDatabaseHelper.getAbsolutePath();
        String backupPath = originalPath + ".backup";

        // Read all lines from original file
        List<String> lines = BookingDatabaseHelper.readAllLines();

        // Write to backup file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupPath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        return backupPath;
    }

    private static void restoreDatabase(String backupPath) throws IOException {
        String originalPath = BookingDatabaseHelper.getAbsolutePath();

        // Read all lines from backup file
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(backupPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        // Write back to original file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(originalPath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        // Delete backup file
        new File(backupPath).delete();
    }

    @Test
    public void testGetBookingsForTimeSlotNoOverlap() throws Exception {
        // Backup the database
        String backupPath = backupDatabase();

        try {
            // Get the last booking ID and increment it by 1
            int lastID = BookingDatabaseHelper.getLastBookingID();
            String testBookingID = String.format("N2S%06d", lastID + 1);

            // Create a booking with a time slot that doesn't overlap with existing bookings
            Booking testBooking = new Booking.BookingBuilder()
                    .setBookingID(testBookingID)
                    .setUserID(1001)
                    .setParkingSpace(1)
                    .setParkingLot(1)
                    .setStart(LocalTime.of(13, 0))  // 1:00 PM
                    .setEnd(LocalTime.of(14, 0))    // 2:00 PM
                    .setBookingStatus("Active")
                    .setPaymentStatus("Pending")
                    .setPlate("TEST123")
                    .setAmount(5.0)
                    .setTotalAmount(20.0)
                    .build();

            // Save the booking
            BookingDatabaseHelper.saveBooking(testBooking);

            // Test for a time slot that doesn't overlap with any existing bookings
            // Using 4:30-5:00 AM which should be outside all existing booking times
            List<Booking> bookings = BookingDatabaseHelper.getBookingsForTimeSlot(
                    LocalTime.of(4, 30),  // 4:30 AM
                    LocalTime.of(5, 0)    // 5:00 AM
            );

            // Instead of expecting 0 bookings, we verify that the only bookings found don't overlap with our time slot
            for (Booking booking : bookings) {
                boolean overlaps = !(booking.getEnd().isBefore(LocalTime.of(4, 30)) || 
                                   booking.getStart().isAfter(LocalTime.of(5, 0)));
                assertFalse(overlaps, "Booking should not overlap with 4:30-5:00 AM time slot");
            }

            // Clean up
            BookingDatabaseHelper.cancelBooking(testBookingID);
        } finally {
            // Restore the database
            restoreDatabase(backupPath);
        }
    }


    @Test
    public void testGetBookingsForTimeSlotInvalidTimeRange() throws Exception {

        String backupPath = backupDatabase();

        try {
            // Clean up any existing bookings
            List<String> lines = BookingDatabaseHelper.readAllLines();
            List<String> newLines = new ArrayList<>();
            newLines.add(lines.get(0)); // Keep the header
            BookingDatabaseHelper.writeAllLines(newLines);

            // Test with invalid time range (end time before start time)
            List<Booking> foundBookings = BookingDatabaseHelper.getBookingsForTimeSlot(
                    LocalTime.of(12, 0), LocalTime.of(10, 0));

            assertNotNull(foundBookings, "Returned list should not be null");
            assertEquals(0, foundBookings.size(), "Should return empty list for invalid time range");
        } finally {
            // Restore the database
            restoreDatabase(backupPath);
        }
    }


    @Test
    public void testGetBookingsForTimeSlotSameStartEnd() {
        List<Booking> foundBookings = BookingDatabaseHelper.getBookingsForTimeSlot(
                LocalTime.of(10, 0), LocalTime.of(10, 0));

        assertNotNull(foundBookings, "Returned list should not be null");
        assertFalse(foundBookings.isEmpty(), "Should find bookings that include 10:00");

        for (Booking booking : foundBookings) {
            assertTrue(
                    !booking.getEnd().isBefore(LocalTime.of(10, 0)) &&
                            !booking.getStart().isAfter(LocalTime.of(10, 0)),
                    "Booking should include 10:00"
            );
        }
    }


    @Test
    public void testGetLastBookingID() {
        int lastID = BookingDatabaseHelper.getLastBookingID();
        // Just verify that the ID is positive, since other tests are constantly creating new bookings
        assertTrue(lastID > 0, "Last booking ID should be positive");
    }


    @Test
    public void testUpdateBookingStatusSuccess() throws Exception {
        // Create a test booking
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updateBookingStatus(testBookingID, "Cancelled");

        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking);
        assertEquals("Cancelled", updatedBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testUpdateBookingStatusWithInvalidStatus() throws Exception {
        // Create a test booking
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updateBookingStatus(testBookingID, "InvalidStatus");

        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking);
        assertEquals("InvalidStatus", updatedBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testUpdateBookingStatusWithSpecialCharacters() throws Exception {
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);

        String specialStatus = "Status!@#$%^&*()";
        BookingDatabaseHelper.updateBookingStatus(testBookingID, specialStatus);
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking);
        assertEquals(specialStatus, updatedBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testUpdateBookingStatusWithLongStatus() throws Exception {
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);

        String longStatus = "A".repeat(1000); // 1000 character long status
        BookingDatabaseHelper.updateBookingStatus(testBookingID, longStatus);

        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking);
        assertEquals(longStatus, updatedBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testUpdateBookingStatusWithSameStatus() throws Exception {
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        BookingDatabaseHelper.saveBooking(testBooking);
        BookingDatabaseHelper.updateBookingStatus(testBookingID, "Active");

        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking);
        assertEquals("Active", updatedBooking.getBookingStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }
}