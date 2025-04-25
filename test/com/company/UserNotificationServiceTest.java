package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

public class UserNotificationServiceTest {
    private UserNotificationService notificationService;
    private Booking testBooking;
    private PriceCalculator calculator;

    @BeforeEach
    public void setUp() throws Exception {
        notificationService = new UserNotificationService();
        calculator = new PriceCalculator();
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

    @Test
    public void testUpdateWithActiveBooking() {
        // Test notification for active booking
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithCancelledBooking() {
        // Test notification for cancelled booking
        testBooking.cancelBooking();
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithCompletedBooking() {
        // Test notification for completed booking
        Payment payment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        payment.setCalculator(calculator);
        payment.setPaymentStatus(true);
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithNullBooking() {
        // Test handling of null booking
        assertThrows(NullPointerException.class, () -> notificationService.update(null));
    }

    @Test
    public void testUpdateWithExtendedBooking() {
        // Test notification for extended booking
        LocalTime newEndTime = LocalTime.of(14, 0);
        testBooking.extendBooking(testBooking.getBookingID(), newEndTime);
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithFailedPayment() {
        // Test notification for failed payment
        Payment payment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        payment.setCalculator(calculator);
        payment.setPaymentStatus(false);
        testBooking.setPayment(payment);
        testBooking.payDeposit(1001);
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithSuccessfulPayment() {
        // Test notification for successful payment
        Payment payment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        payment.setCalculator(calculator);
        payment.setPaymentStatus(true);
        testBooking.setPayment(payment);
        testBooking.payDeposit(1001);
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithMultipleStatusChanges() {
        // Test multiple status changes in sequence
        assertDoesNotThrow(() -> notificationService.update(testBooking));

        testBooking.cancelBooking();
        assertDoesNotThrow(() -> notificationService.update(testBooking));

        Payment payment = new Payment("TEST001", 20.0, 5.0, "Credit Card", null);
        payment.setCalculator(calculator);
        payment.setPaymentStatus(true);
        testBooking.setPayment(payment);
        testBooking.payDeposit(1001);
        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithInvalidBookingStatus() throws Exception {
        // Test handling of invalid booking status
        testBooking = new Booking.BookingBuilder()
            .setBookingID(String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1))
            .setUserID(1001)
            .setParkingSpace(1)
            .setParkingLot(1)
            .setStart(LocalTime.of(10, 0))
            .setEnd(LocalTime.of(12, 0))
            .setBookingStatus("InvalidStatus")
            .setPaymentStatus("Pending")
            .setPlate("TEST123")
            .setAmount(5.0)
            .setTotalAmount(20.0)
            .build();

        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }

    @Test
    public void testUpdateWithInvalidPaymentStatus() throws Exception {
        // Test handling of invalid payment status
        testBooking = new Booking.BookingBuilder()
            .setBookingID(String.format("N2S%06d", BookingDatabaseHelper.getLastBookingID() + 1))
            .setUserID(1001)
            .setParkingSpace(1)
            .setParkingLot(1)
            .setStart(LocalTime.of(10, 0))
            .setEnd(LocalTime.of(12, 0))
            .setBookingStatus("Active")
            .setPaymentStatus("InvalidStatus")
            .setPlate("TEST123")
            .setAmount(5.0)
            .setTotalAmount(20.0)
            .build();

        assertDoesNotThrow(() -> notificationService.update(testBooking));
    }
}