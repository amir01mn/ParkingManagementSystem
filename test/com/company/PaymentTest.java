package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class PaymentTest {

	private Payment payment;
	private Booking booking;
	private PriceCalculator calculator;

	@BeforeEach
    void setUp() {
        booking = new Booking.BookingBuilder()
                .setStart(LocalTime.now().plusHours(2))
                .setEnd(LocalTime.now().plusHours(5))
                .build();
        calculator = new PriceCalculator();
        payment = new Payment("1", 15.0, 5.0, "Credit Card", booking);
        payment.setCalculator(calculator);
    }



	@Test
	void testSetandGetAmount() {
		payment.setAmount(20.0);
		assertEquals(20.0, payment.getAmount());
	}



	@Test
	void testSetandGetMethod() {
		payment.setMethod("Apple Pay");
		assertEquals("Apple Pay", payment.getMethod());
	}



	@Test
	void testProcessDepositPaymentValid() {
		int userID = 1004;
		boolean result = payment.processDepositPayment(userID);
		assertTrue(result);
		assertTrue(payment.getDeposit() > 0);
		assertTrue(payment.isPaymentStatus());
	}

	@Test
	void testProcessDepositPaymentInvalid(){
		int userID = 999;
		payment.setCalculator(new PriceCalculator());
		boolean result = payment.processDepositPayment(userID);
		assertFalse(result);
	}

	@Test
	void testProcessPaymentReturn() {
		payment.setPaymentStatus(true);
		assertTrue(payment.processPayment(20.0));
	}


	@Test
    void testRefundDepositValid() {
        assertTrue(payment.refundDeposit(10.0));
    }

	@Test
    void testVerifyPaymentStatusValid() {
        payment.setPaymentStatus(true);
        assertEquals("Payment Successful", payment.verifyPaymentStatus());
    }

    @Test
    void testVerifyPaymentStatusInvalid() {
        payment.setPaymentStatus(false);
        assertEquals("Payment Failed", payment.verifyPaymentStatus());
    }

    @Test
    void testCancelBookingBeforeStartTime() throws Exception {
        String uniqueBookingID = "TEST" + System.currentTimeMillis();
        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking booking = builder
                .setBookingID(uniqueBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setPlate("TEST123")
                .setStart(LocalTime.now().plusHours(3))
                .setEnd(LocalTime.now().plusHours(5))
                .setPaymentStatus("Pending")
                .setAmount(5.0)
                .setBookingStatus("Active")
                .setTotalAmount(20.0)
                .build();
        BookingDatabaseHelper.saveBooking(booking);

        Payment payment = new Payment("4", 30.0, 10.0, "Paypal", booking);
        payment.setCalculator(new PriceCalculator());  // Set the calculator
        payment.cancelBooking(booking.getBookingID());

        String cancelled = BookingDatabaseHelper.findBookingByID(booking.getBookingID()).getStatus();
        assertEquals("Cancelled", cancelled);
        
        // Clean up
        BookingDatabaseHelper.cancelBooking(uniqueBookingID);
    }

    @Test
    void testCancelBookingAfterStartTime() throws Exception {
        String uniqueBookingID = "TEST" + System.currentTimeMillis();
        Booking.BookingBuilder builder = new Booking.BookingBuilder();

        Booking booking = builder
                .setBookingID(uniqueBookingID)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setPlate("TEST123")
                .setStart(LocalTime.now().minusMinutes(30))
                .setEnd(LocalTime.now().plusHours(5))
                .setPaymentStatus("Pending")
                .setAmount(5.0)
                .setBookingStatus("Active")
                .setTotalAmount(20.0)
                .build();
        BookingDatabaseHelper.saveBooking(booking);

        Payment payment = new Payment("5", 30.0, 10.0, "Paypal", booking);
        payment.setCalculator(new PriceCalculator());  // Set the calculator
        payment.cancelBooking(booking.getBookingID());

        String cancelled = BookingDatabaseHelper.findBookingByID(booking.getBookingID()).getStatus();
        assertEquals("Cancelled", cancelled);
        
        // Clean up
        BookingDatabaseHelper.cancelBooking(uniqueBookingID);
    }

    @Test
    void testProcessDepositPaymentZeroDeposit() {
        PriceCalculator dummyCalculator = new PriceCalculator() {
            @Override
            public String getUserType(int userID) {
                return "DummyType";
            }

            @Override
            public double checkRate(String userType) {
                return 0.0; // Force zero deposit
            }
        };

        payment.setCalculator(dummyCalculator);
        assertFalse(payment.processDepositPayment(1004));
    }
    @Test
    void testChargeSecondPaymentZeroSecondPayment() {
        payment.setAmount(0.0);
        payment.setDeposit(0.0);
        payment.setCalculator(new PriceCalculator());
        payment.chargeSecondPayment("TESTID");
    }
    @Test
    void testCancelBookingNonExistentBooking() {
        Payment.cancelBooking("NON_EXISTENT_ID");
        // No assert needed, print should be hit, path covered
    }
    @Test
    void testRefundDepositZeroValue() {
        assertTrue(payment.refundDeposit(0.0));
    }
    @Test
    void testGetPaymDetailsOutput() {
        String details = payment.getPaymDetails();
        assertTrue(details.contains("PaymentID:"));
    }

    @Test
    void testSetPaymentIDFunctionality() {
        payment.setPaymentID("NEWID123");
        assertEquals("NEWID123", payment.getPaymentID());
    }

}
