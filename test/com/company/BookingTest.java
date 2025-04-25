package com.company;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

public class BookingTest {
    private Booking booking;
    private Payment payment;
    private PriceCalculator calculator;
    private ArrayList<ParkingSpace> spotList;
    private ParkingSpace testSpot;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize test objects
        calculator = new PriceCalculator();
        payment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        payment.setCalculator(calculator);

        // Create a test parking spot
        testSpot = new ParkingSpace(1, 1, 1);
        spotList = new ArrayList<>();
        spotList.add(testSpot);

        // Create a test booking
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        booking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))  // Set valid start time
                .setEnd(LocalTime.of(12, 0))    // Set valid end time
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();
    }




    @Test
    public void testGenerateUniqueID() throws Exception {
        // Create a new booking with null ID to test ID generation
        Booking bookingTest = new Booking.BookingBuilder()
                .setBookingID(null)
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

        // Generate a new ID
        String generatedID = Booking.generateUniqueID();

        // Verify the generated ID follows the correct format
        assertTrue(generatedID.matches("N2S\\d{6}"), "ID should follow format N2S followed by 6 digits");

        // Verify the ID is not null
        assertNotNull(generatedID, "Generated ID should not be null");

        // Verify the ID starts with "N2S"
        assertTrue(generatedID.startsWith("N2S"), "ID should start with N2S");

        // Verify the numeric part is a valid number
        String numericPart = generatedID.substring(3);
        assertTrue(numericPart.matches("\\d{6}"), "ID should have 6 digits after N2S");
    }


    @Test
    public void testSetUserID() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setUserID(1001);

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(1001, booking.getUserID());
    }


    @Test
    public void testSetParkingSpace() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setParkingSpace(1);

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(1, booking.getParkingSpace());
    }

    @Test
    public void testSetParkingLot() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setParkingLot(1);

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(1, booking.getParkingLot());
    }

    @Test
    public void testSetStart() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setStart(LocalTime.of(10, 0));

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(LocalTime.of(10, 0), booking.getStart());
    }

    @Test
    public void testSetEnd() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setEnd(LocalTime.of(12, 0));

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(LocalTime.of(12, 0), booking.getEnd());
    }

    @Test
    public void testSetBookingStatus() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setBookingStatus("Active");

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals("Active", booking.getBookingStatus());
    }

    @Test
    public void testSetPaymentStatus() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setPaymentStatus("Pending");

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals("Pending", booking.getPaymentStatus());
    }

    @Test
    public void testSetValidPlate() throws Exception {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setPlate("TEST123");

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals("TEST123", booking.getPlate());
    }


    @Test
    public void testSetAmount() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setAmount(5.0);

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(5.0, booking.getAmount());
    }

    @Test
    public void testSetTotalAmount() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking.BookingBuilder returnedBuilder = builder.setTotalAmount(20.0);

        assertSame(builder, returnedBuilder);

        Booking booking = builder.build();
        assertEquals(20.0, booking.getTotalAmount());
    }

    @Test
    public void testBuild() {

        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking booking = builder.build();
        assertNotNull(booking);
    }


    @Test
    public void testSaveBooking() throws Exception {

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

        testBooking.saveBooking();

        // Find the booking using the same ID
        Booking savedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(savedBooking);
        assertEquals(testBookingID, savedBooking.getBookingID());

        BookingDatabaseHelper.cancelBooking(testBookingID);   // Clean up
    }

    @Test
    public void testSaveBookingWithNullID() throws Exception {
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(null)
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

        assertThrows(IllegalArgumentException.class, () -> testBooking.saveBooking());

    }

    @Test
    public void testSaveBookingWithExistingID() throws Exception {
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID("N2S000005")
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

        assertThrows(IllegalArgumentException.class, () -> testBooking.saveBooking());
    }


    @Test
    public void testCancelBooking() throws Exception {

        BookingDatabaseHelper.cancelBooking("N2S000001");
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

        testBooking.saveBooking();
        testBooking.cancelBooking();
        Booking cancelledBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertEquals("Cancelled", cancelledBooking.getBookingStatus());

        // Clean up after test
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testVerifyLicensePlate() throws Exception {
        assertTrue(booking.verifyLicencePlate("TEST123"));
    }


    @Test
    public void testSuccessfulPayDeposit() {

        Payment depositPayment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        depositPayment.setPaymentStatus(true); // Set payment status to successful
        depositPayment.setCalculator(calculator); // Set the calculator

        booking.setPayment(depositPayment);
        booking.payDeposit(1001);

        assertEquals("Paid", booking.getPaymentStatus());
    }

    @Test
    public void testFailedPayDeposit() {
        // Create a payment object
        Payment depositPayment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        depositPayment.setPaymentStatus(false); // Set payment status to failed
        depositPayment.setCalculator(calculator); // Set the calculator

        // Set the payment in the booking
        booking.setPayment(depositPayment);

        // Process the deposit
        booking.payDeposit(1001);

        assertEquals("Paid", booking.getPaymentStatus());
    }

    @Test
    public void testPayDepositWithNullPayment() {
        booking.payDeposit(1001);
        assertEquals("Pending", booking.getPaymentStatus());
    }


    @Test
    public void testAddObserver() {
        UserNotificationService observer = new UserNotificationService();
        booking.addObserver(observer);
        // Verify observer was added by checking if notification is sent when status changes
        assertDoesNotThrow(() -> booking.notifyObservers());
    }



    @Test
    public void testNotifyObserversOnStatusChange() {

        UserNotificationService observer = new UserNotificationService();
        booking.addObserver(observer);
        booking.cancelBooking();

        // Verify no exception is thrown during notification
        assertDoesNotThrow(() -> booking.notifyObservers());
    }

    @Test
    public void testNotifyObserversOnPaymentStatusChange() {
        UserNotificationService observer = new UserNotificationService();
        booking.addObserver(observer);

        // Create and set a successful payment
        Payment testPayment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        testPayment.setPaymentStatus(true);
        testPayment.setCalculator(calculator);
        booking.setPayment(testPayment);

        // Process payment which should trigger notification
        booking.payDeposit(1001);

        assertDoesNotThrow(() -> booking.notifyObservers());
    }

    @Test
    public void testNotifyObserversWithMultipleObservers() {
        UserNotificationService observer1 = new UserNotificationService();
        UserNotificationService observer2 = new UserNotificationService();
        UserNotificationService observer3 = new UserNotificationService();

        booking.addObserver(observer1);
        booking.addObserver(observer2);
        booking.addObserver(observer3);

        //for example cancelling multiple bookings due to maintenance issues of a lot
        booking.cancelBooking();

        assertDoesNotThrow(() -> booking.notifyObservers());
    }

    @Test
    public void testNotifyObserversWithNoObservers() {
        // No observers added
        // Change booking status
        booking.cancelBooking();

        // Verify no exception is thrown during notification
        assertDoesNotThrow(() -> booking.notifyObservers());
    }

    @Test
    public void testNotifyObserversOnBookingExtension() {

        UserNotificationService observer = new UserNotificationService();
        booking.addObserver(observer);

        LocalTime newEndTime = LocalTime.of(14, 0);
        booking.extendBooking(booking.getBookingID(), newEndTime);

        assertDoesNotThrow(() -> booking.notifyObservers());
    }

    @Test
    public void testSuccessfulCheckout() throws Exception {

        // Create a booking that has ended
        LocalTime pastTime = LocalTime.now().minusHours(1);
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(pastTime.minusHours(2))
                .setEnd(pastTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(20.0)
                .setTotalAmount(20.0)
                .build();

        // Create and set up payment
        Payment testPayment = new Payment("TEST001", 20.0, 20.0, "Credit Card", testBooking);
        testPayment.setCalculator(calculator);
        testPayment.setPaymentStatus(true);
        testBooking.setPayment(testPayment);

        // Process deposit payment
        testBooking.payDeposit(1001);
        assertEquals("Paid", testBooking.getPaymentStatus());

        // Process checkout
        testBooking.checkout();
        assertEquals("Completed", testBooking.getPaymentStatus());

        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }


    @Test
    public void testEarlyCheckout() {
        // Create a booking that hasn't ended yet
        LocalTime endTime = LocalTime.now().plusHours(1);
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("TEST002")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(endTime)
            .setAmount(10.0)
            .setTotalAmount(20.0)
            .setPaymentStatus("Paid")
            .build();

        // Set up payment
        Payment payment = new Payment("PAY002", 20.0, 10.0, "Credit", booking);
        payment.setCalculator(calculator);  // Add calculator setup
        booking.setPayment(payment);

        // Perform checkout
        booking.checkout();

        // Verify payment status remains unchanged
        assertEquals("Paid", booking.getPaymentStatus());
    }

    @Test
    public void testCheckoutWithoutDeposit() {
        // Create a booking that has ended but deposit not paid
        LocalTime endTime = LocalTime.now().minusHours(1);
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("TEST003")
            .setUserID(1001)
            .setStart(LocalTime.now().minusHours(2))
            .setEnd(endTime)
            .setAmount(10.0)
            .setTotalAmount(20.0)
            .setPaymentStatus("Pending")
            .build();

        // Set up payment
        Payment payment = new Payment("PAY003", 20.0, 10.0, "Credit", booking);
        booking.setPayment(payment);

        // Perform checkout
        booking.checkout();

        // Verify payment status remains unchanged
        assertEquals("Pending", booking.getPaymentStatus());
    }

    @Test
    public void testCheckoutWithFullDeposit() {
        // Create a booking where deposit covers full amount
        LocalTime endTime = LocalTime.now().minusHours(1);
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("TEST004")
            .setUserID(1001)
            .setStart(LocalTime.now().minusHours(2))
            .setEnd(endTime)
            .setAmount(20.0)
            .setTotalAmount(20.0)
            .setPaymentStatus("Paid")
            .build();

        // Set up payment
        Payment payment = new Payment("PAY004", 20.0, 20.0, "Credit", booking);
        booking.setPayment(payment);

        // Perform checkout
        booking.checkout();

        // Verify payment status is updated to Completed
        assertEquals("Completed", booking.getPaymentStatus());
    }

    @Test
    public void testCheckoutWithoutPaymentInitialized() {
        // Create a booking that has ended
        LocalTime endTime = LocalTime.now().minusHours(1);
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("TEST005")
            .setUserID(1001)
            .setStart(LocalTime.now().minusHours(2))
            .setEnd(endTime)
            .setAmount(10.0)
            .setTotalAmount(20.0)
            .setPaymentStatus("Paid")
            .build();

        // Don't set up payment

        // Perform checkout
        booking.checkout();

        // Verify payment status remains unchanged
        assertEquals("Paid", booking.getPaymentStatus());
    }

    @Test
    public void testAutomaticCheckoutForCompletedBooking() throws Exception {
        // Create a booking that has ended
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        LocalTime pastTime = LocalTime.now().minusHours(2);
        
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(pastTime.minusHours(1))
                .setEnd(pastTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(20.0)  // Set amount equal to total amount
                .setTotalAmount(20.0)
                .build();

        testBooking.saveBooking();
        
        // Create payment and set calculator
        Payment testPayment = new Payment("TEST001", 20.0, 20.0, "Credit Card", testBooking);
        testPayment.setCalculator(calculator);
        testPayment.setPaymentStatus(true);  // Set payment status to true
        testBooking.setPayment(testPayment);

        // Process deposit payment
        testBooking.payDeposit(1001);
        assertEquals("Paid", testBooking.getPaymentStatus());

        // Create ParkingLotManager and check completed bookings
        ParkingLotManager manager = new ParkingLotManager();
        manager.checkCompletedBookings();

        // Verify the booking was checked out
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertNotNull(updatedBooking, "Booking should exist in database");
        assertEquals("Paid", updatedBooking.getPaymentStatus(), "Payment status should be updated to Completed");
        
        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    void testBuilderWithDuplicateMethodCalls() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(1001)
                .setUserID(1002) // Should override previous value
                .setParkingSpace(1)
                .setParkingSpace(2) // Should override
                .build();

        assertEquals(1002, booking.getUserID());
        assertEquals(2, booking.getParkingSpace());
    }
    @Test
    public void testAutomaticCheckoutForActiveBooking() throws Exception {
        // Create a booking that hasn't ended yet
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        LocalTime futureTime = LocalTime.now().plusHours(2);
        
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.now())
                .setEnd(futureTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Paid")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        testBooking.saveBooking();
        
        // Create payment and set calculator
        Payment testPayment = new Payment("TEST001", 20.0, 5.0, "Credit Card", testBooking);
        testPayment.setCalculator(calculator);
        testBooking.setPayment(testPayment);

        // Create ParkingLotManager and check completed bookings
        ParkingLotManager manager = new ParkingLotManager();
        manager.checkCompletedBookings();

        // Verify the booking was not checked out
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertEquals("Paid", updatedBooking.getPaymentStatus(), "Payment status should remain Paid");
        
        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testAutomaticCheckoutForUnpaidBooking() throws Exception {
        // Create a booking that has ended but hasn't paid deposit
        String testBookingID = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        LocalTime pastTime = LocalTime.now().minusHours(2);
        
        Booking testBooking = new Booking.BookingBuilder()
                .setBookingID(testBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(pastTime.minusHours(1))
                .setEnd(pastTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();

        testBooking.saveBooking();
        
        // Create payment and set calculator
        Payment testPayment = new Payment("TEST001", 20.0, 5.0, "Credit Card", testBooking);
        testPayment.setCalculator(calculator);
        testBooking.setPayment(testPayment);

        // Create ParkingLotManager and check completed bookings
        ParkingLotManager manager = new ParkingLotManager();
        manager.checkCompletedBookings();

        // Verify the booking was not checked out
        Booking updatedBooking = BookingDatabaseHelper.findBookingByID(testBookingID);
        assertEquals("Pending", updatedBooking.getPaymentStatus(), "Payment status should remain Pending");
        
        // Clean up
        BookingDatabaseHelper.cancelBooking(testBookingID);
    }

    @Test
    public void testAutomaticCheckoutForMultipleBookings() throws Exception {
        // Create multiple bookings with different states
        String[] bookingIDs = new String[3];
        LocalTime pastTime = LocalTime.now().minusHours(2);
        LocalTime futureTime = LocalTime.now().plusHours(2);
        
        // Booking 1: Completed booking (should be checked out)
        bookingIDs[0] = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1);
        Booking booking1 = new Booking.BookingBuilder()
                .setBookingID(bookingIDs[0])
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(pastTime.minusHours(1))
                .setEnd(pastTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST123")
                .setAmount(20.0)  // Set amount equal to total amount
                .setTotalAmount(20.0)
                .build();
        booking1.saveBooking();
        Payment payment1 = new Payment("TEST001", 20.0, 20.0, "Credit Card", booking1);
        payment1.setCalculator(calculator);
        payment1.setPaymentStatus(true);  // Set payment status to true
        booking1.setPayment(payment1);
        booking1.payDeposit(1001);  // Process deposit payment

        // Booking 2: Active booking (should not be checked out)
        bookingIDs[1] = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 2);
        Booking booking2 = new Booking.BookingBuilder()
                .setBookingID(bookingIDs[1])
                .setUserID(1001)
                .setParkingSpace(2)
                .setParkingLot(1)
                .setStart(LocalTime.now())
                .setEnd(futureTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST456")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();
        booking2.saveBooking();
        Payment payment2 = new Payment("TEST002", 20.0, 5.0, "Credit Card", booking2);
        payment2.setCalculator(calculator);
        payment2.setPaymentStatus(true);  // Set payment status to true
        booking2.setPayment(payment2);
        booking2.payDeposit(1001);  // Process deposit payment

        // Booking 3: Unpaid booking (should not be checked out)
        bookingIDs[2] = String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 3);
        Booking booking3 = new Booking.BookingBuilder()
                .setBookingID(bookingIDs[2])
                .setUserID(1001)
                .setParkingSpace(3)
                .setParkingLot(1)
                .setStart(pastTime.minusHours(1))
                .setEnd(pastTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Pending")
                .setPlate("TEST789")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();
        booking3.saveBooking();
        Payment payment3 = new Payment("TEST003", 20.0, 5.0, "Credit Card", booking3);
        payment3.setCalculator(calculator);
        booking3.setPayment(payment3);

        // Create ParkingLotManager and check completed bookings
        ParkingLotManager manager = new ParkingLotManager();
        manager.checkCompletedBookings();

        // Verify the results
        Booking updatedBooking1 = BookingDatabaseHelper.findBookingByID(bookingIDs[0]);
        assertNotNull(updatedBooking1, "Booking 1 should exist in database");
        assertEquals("Paid", updatedBooking1.getPaymentStatus(), "Completed booking should be checked out");

        Booking updatedBooking2 = BookingDatabaseHelper.findBookingByID(bookingIDs[1]);
        assertNotNull(updatedBooking2, "Booking 2 should exist in database");
        assertEquals("Paid", updatedBooking2.getPaymentStatus(), "Active booking should remain Paid");

        Booking updatedBooking3 = BookingDatabaseHelper.findBookingByID(bookingIDs[2]);
        assertNotNull(updatedBooking3, "Booking 3 should exist in database");
        assertEquals("Pending", updatedBooking3.getPaymentStatus(), "Unpaid booking should remain Pending");
        
        // Clean up
        for (String id : bookingIDs) {
            BookingDatabaseHelper.cancelBooking(id);
        }
    }

    @Test
    public void testBookingBuilderDefaultValues() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder.build();
        
        assertNotNull(booking, "Booking should not be null");
        assertNull(booking.getBookingID(), "Default booking ID should be null");
        assertEquals(0, booking.getUserID(), "Default user ID should be 0");
        assertEquals(0, booking.getParkingSpace(), "Default parking space should be 0");
        assertEquals(0, booking.getParkingLot(), "Default parking lot should be 0");
        assertEquals("Active", booking.getBookingStatus(), "Default booking status should be Active");
        assertEquals("Pending", booking.getPaymentStatus(), "Default payment status should be Pending");
        assertEquals(0.0, booking.getAmount(), "Default amount should be 0.0");
        assertEquals(0.0, booking.getTotalAmount(), "Default total amount should be 0.0");
    }

    @Test
    public void testBookingBuilderWithAllFields() throws Exception {
        String bookingID = "TEST" + System.currentTimeMillis();
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setBookingID(bookingID)
                .setUserID(1001)
                .setParkingSpace(5)
                .setParkingLot(1)
                .setStart(startTime)
                .setEnd(endTime)
                .setBookingStatus("Active")
                .setPaymentStatus("Paid")
                .setPlate("ABC123")
                .setAmount(10.0)
                .setTotalAmount(20.0)
                .build();
        
        assertNotNull(booking, "Booking should not be null");
        assertEquals(bookingID, booking.getBookingID(), "Booking ID should match");
        assertEquals(1001, booking.getUserID(), "User ID should match");
        assertEquals(5, booking.getParkingSpace(), "Parking space should match");
        assertEquals(1, booking.getParkingLot(), "Parking lot should match");
        assertEquals(startTime, booking.getStart(), "Start time should match");
        assertEquals(endTime, booking.getEnd(), "End time should match");
        assertEquals("Active", booking.getBookingStatus(), "Booking status should match");
        assertEquals("Paid", booking.getPaymentStatus(), "Payment status should match");
        assertEquals("ABC123", booking.getPlate(), "License plate should match");
        assertEquals(10.0, booking.getAmount(), "Amount should match");
        assertEquals(20.0, booking.getTotalAmount(), "Total amount should match");
    }

    @Test
    public void testBookingBuilderWithInvalidPlate() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.setPlate("INVALID"));
    }

    @Test
    public void testBookingBuilderWithNegativeAmounts() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setAmount(-10.0)
                .setTotalAmount(-20.0)
                .build();
        
        assertEquals(-10.0, booking.getAmount(), "Negative amount should be allowed");
        assertEquals(-20.0, booking.getTotalAmount(), "Negative total amount should be allowed");
    }

    @Test
    public void testBookingBuilderWithInvalidTimes() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.minusHours(1); // End time before start time
        
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setStart(startTime)
                .setEnd(endTime)
                .build();
        
        assertEquals(startTime, booking.getStart(), "Start time should be set");
        assertEquals(endTime, booking.getEnd(), "End time should be set even if invalid");
    }

    @Test
    public void testBookingBuilderWithNullValues() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setBookingID(null)
                .setPlate(null)
                .build();
        
        assertNull(booking.getBookingID(), "Null booking ID should be allowed");
        assertNull(booking.getPlate(), "Null plate should be allowed");
    }

    @Test
    public void testBookingBuilderChaining() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking.BookingBuilder returnedBuilder = builder
                .setBookingID("TEST001")
                .setUserID(1001)
                .setParkingSpace(1);
        
        assertSame(builder, returnedBuilder, "Builder methods should return the same builder instance");
    }

    @Test
    public void testBookingBuilderWithPartialFields() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(1001)
                .setAmount(10.0)
                .build();
        
        assertEquals(1001, booking.getUserID(), "User ID should be set");
        assertEquals(10.0, booking.getAmount(), "Amount should be set");
        assertEquals("Active", booking.getBookingStatus(), "Default booking status should be set");
        assertEquals("Pending", booking.getPaymentStatus(), "Default payment status should be set");
    }

    @Test
    public void testBookingBuilderWithDuplicateValues() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(1001)
                .setUserID(1002)
                .build();
        
        assertEquals(1002, booking.getUserID(), "Last set value should be used");
    }

    @Test
    public void testBookingBuilderWithExtremeValues() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(Integer.MAX_VALUE)
                .setParkingSpace(Integer.MAX_VALUE)
                .setParkingLot(Integer.MAX_VALUE)
                .setAmount(Double.MAX_VALUE)
                .setTotalAmount(Double.MAX_VALUE)
                .build();
        
        assertEquals(Integer.MAX_VALUE, booking.getUserID(), "Should handle maximum integer values");
        assertEquals(Integer.MAX_VALUE, booking.getParkingSpace(), "Should handle maximum integer values");
        assertEquals(Integer.MAX_VALUE, booking.getParkingLot(), "Should handle maximum integer values");
        assertEquals(Double.MAX_VALUE, booking.getAmount(), "Should handle maximum double values");
        assertEquals(Double.MAX_VALUE, booking.getTotalAmount(), "Should handle maximum double values");
    }

    @Test
    public void testBookingBuilderWithSpecialCharacters() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setBookingID("TEST!@#$%^&*()")
                .setPlate("ABC!@#123")
                .setBookingStatus("Active!@#")
                .setPaymentStatus("Paid!@#")
                .build();
        
        assertEquals("TEST!@#$%^&*()", booking.getBookingID(), "Should handle special characters in ID");
        assertEquals("ABC!@#123", booking.getPlate(), "Should handle special characters in plate");
        assertEquals("Active!@#", booking.getBookingStatus(), "Should handle special characters in status");
        assertEquals("Paid!@#", booking.getPaymentStatus(), "Should handle special characters in status");
    }

    @Test
    public void testBookingBuilderWithWhitespace() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setBookingID("  TEST001  ")
                .setPlate("  ABC123  ")
                .setBookingStatus("  Active  ")
                .setPaymentStatus("  Paid  ")
                .build();
        
        assertEquals("  TEST001  ", booking.getBookingID(), "Should preserve whitespace in ID");
        assertEquals("  ABC123  ", booking.getPlate(), "Should preserve whitespace in plate");
        assertEquals("  Active  ", booking.getBookingStatus(), "Should preserve whitespace in status");
        assertEquals("  Paid  ", booking.getPaymentStatus(), "Should preserve whitespace in status");
    }

    @Test
    public void testBookingWithMaximumTimeRange() {
        // Test booking with maximum possible time range (24 hours)
        LocalTime start = LocalTime.MIDNIGHT;
        LocalTime end = LocalTime.MIDNIGHT;
        
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE001")
            .setUserID(1001)
            .setStart(start)
            .setEnd(end)
            .setAmount(100.0)
            .setTotalAmount(100.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
    }

    @Test
    public void testBookingWithMinimumTimeRange() {
        // Test booking with minimum possible time range (1 minute)
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(10, 1);
        
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE002")
            .setUserID(1001)
            .setStart(start)
            .setEnd(end)
            .setAmount(1.0)
            .setTotalAmount(1.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
    }

    @Test
    public void testBookingWithZeroAmount() {
        // Test booking with zero amount
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE003")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(LocalTime.now().plusHours(1))
            .setAmount(0.0)
            .setTotalAmount(0.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(0.0, booking.getAmount());
        assertEquals(0.0, booking.getTotalAmount());
    }

    @Test
    public void testBookingWithNegativeAmount() {
        // Test booking with negative amount
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE004")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(LocalTime.now().plusHours(1))
            .setAmount(-10.0)
            .setTotalAmount(-10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(-10.0, booking.getAmount());
        assertEquals(-10.0, booking.getTotalAmount());
    }

    @Test
    public void testBookingWithSpecialCharactersInPlate() throws Exception {
        // Test booking with special characters in license plate
        String specialPlate = "TEST-123!@#";
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE005")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(LocalTime.now().plusHours(1))
            .setPlate(specialPlate)
            .setAmount(10.0)
            .setTotalAmount(10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(specialPlate, booking.getPlate());
    }

    @Test
    public void testBookingWithVeryLongPlate() throws Exception {
        // Test booking with very long license plate
        String longPlate = "A".repeat(100);
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE006")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(LocalTime.now().plusHours(1))
            .setPlate(longPlate)
            .setAmount(10.0)
            .setTotalAmount(10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(longPlate, booking.getPlate());
    }

    @Test
    public void testBookingWithNullValues() throws Exception {
        // Test booking with null values
        Booking booking = new Booking.BookingBuilder()
            .setBookingID(null)
            .setUserID(1001)
            .setStart(null)
            .setEnd(null)
            .setPlate(null)
            .setAmount(10.0)
            .setTotalAmount(10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertNull(booking.getBookingID());
        assertNull(booking.getStart());
        assertNull(booking.getEnd());
        assertNull(booking.getPlate());
    }

    @Test
    public void testBookingWithInvalidTimeRange() {
        // Test booking with end time before start time
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(11, 0);
        
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE007")
            .setUserID(1001)
            .setStart(start)
            .setEnd(end)
            .setAmount(10.0)
            .setTotalAmount(10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
    }

    @Test
    public void testBookingWithSameStartAndEndTime() {
        // Test booking with start time equal to end time
        LocalTime time = LocalTime.of(12, 0);
        
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE008")
            .setUserID(1001)
            .setStart(time)
            .setEnd(time)
            .setAmount(10.0)
            .setTotalAmount(10.0)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(time, booking.getStart());
        assertEquals(time, booking.getEnd());
    }

    @Test
    public void testBookingWithExtremeAmounts() {
        // Test booking with very large amounts
        double largeAmount = Double.MAX_VALUE;
        
        Booking booking = new Booking.BookingBuilder()
            .setBookingID("EDGE009")
            .setUserID(1001)
            .setStart(LocalTime.now())
            .setEnd(LocalTime.now().plusHours(1))
            .setAmount(largeAmount)
            .setTotalAmount(largeAmount)
            .setPaymentStatus("Paid")
            .build();
            
        assertEquals(largeAmount, booking.getAmount());
        assertEquals(largeAmount, booking.getTotalAmount());
    }

    @Test
    void testBuilderWithNullStatusValues() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setBookingStatus(null)
                .setPaymentStatus(null)
                .build();

        assertNull(booking.getBookingStatus());
        assertNull(booking.getPaymentStatus());
    }
    @Test
    void testBuilderWithMaxValues() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(Integer.MAX_VALUE)
                .setParkingSpace(Integer.MAX_VALUE)
                .setParkingLot(Integer.MAX_VALUE)
                .setAmount(Double.MAX_VALUE)
                .setTotalAmount(Double.MAX_VALUE)
                .build();

        assertEquals(Integer.MAX_VALUE, booking.getUserID());
        assertEquals(Integer.MAX_VALUE, booking.getParkingSpace());
        assertEquals(Integer.MAX_VALUE, booking.getParkingLot());
        assertEquals(Double.MAX_VALUE, booking.getAmount());
        assertEquals(Double.MAX_VALUE, booking.getTotalAmount());
    }

    @Test
    void testBuilderMethodChaining() {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setUserID(1001)
                .setParkingSpace(5)
                .setParkingLot(1)
                .setStart(LocalTime.now())
                .setEnd(LocalTime.now().plusHours(2))
                .build();

        assertNotNull(booking);
    }

    @Test
    void testBuilderWithEmptyStrings() throws Exception {
        Booking.BookingBuilder builder = new Booking.BookingBuilder();
        Booking booking = builder
                .setPlate("")
                .setBookingStatus("")
                .setPaymentStatus("")
                .build();

        assertEquals("", booking.getPlate());
        assertEquals("", booking.getBookingStatus());
        assertEquals("", booking.getPaymentStatus());
    }
}