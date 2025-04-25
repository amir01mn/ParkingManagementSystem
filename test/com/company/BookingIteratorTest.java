package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class BookingIteratorTest {
    private List<Booking> bookings;
    private PriceCalculator calculator;

    @BeforeEach
    public void setUp() throws Exception {
        calculator = new PriceCalculator();
        bookings = new ArrayList<>();

        // Create test bookings with different statuses
        bookings.add(createTestBooking("N2S000001", "Active", "Pending"));
        bookings.add(createTestBooking("N2S000002", "Cancelled", "Failed"));
        bookings.add(createTestBooking("N2S000003", "Completed", "Paid"));
        bookings.add(createTestBooking("N2S000004", "Active", "Pending"));
        bookings.add(createTestBooking("N2S000005", "Cancelled", "Failed"));
    }

    private Booking createTestBooking(String id, String bookingStatus, String paymentStatus) throws Exception {
        return new Booking.BookingBuilder()
                .setBookingID(id)
                .setUserID(1001)
                .setParkingSpace(1)
                .setParkingLot(1)
                .setStart(LocalTime.of(10, 0))
                .setEnd(LocalTime.of(12, 0))
                .setBookingStatus(bookingStatus)
                .setPaymentStatus(paymentStatus)
                .setPlate("TEST123")
                .setAmount(5.0)
                .setTotalAmount(20.0)
                .build();
    }

    @Test
    public void testIteratorWithNoFilter() {
        // Test iterator with no filter (should return all bookings)
        BookingIterator iterator = new BookingIterator(bookings, booking -> true);

        int count = 0;
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            assertNotNull(booking);
            count++;
        }

        assertEquals(bookings.size(), count, "Should iterate through all bookings");
    }

    @Test
    public void testIteratorWithActiveBookingsFilter() {
        // Test iterator with filter for active bookings
        Predicate<Booking> activeFilter = booking -> "Active".equals(booking.getBookingStatus());
        BookingIterator iterator = new BookingIterator(bookings, activeFilter);

        int count = 0;
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            assertEquals("Active", booking.getBookingStatus());
            count++;
        }

        assertEquals(2, count, "Should find 2 active bookings");
    }

    @Test
    public void testIteratorWithCancelledBookingsFilter() {
        // Test iterator with filter for cancelled bookings
        Predicate<Booking> cancelledFilter = booking -> "Cancelled".equals(booking.getBookingStatus());
        BookingIterator iterator = new BookingIterator(bookings, cancelledFilter);

        int count = 0;
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            assertEquals("Cancelled", booking.getBookingStatus());
            count++;
        }

        assertEquals(2, count, "Should find 2 cancelled bookings");
    }

    @Test
    public void testIteratorWithCompletedBookingsFilter() {
        // Test iterator with filter for completed bookings
        Predicate<Booking> completedFilter = booking -> "Completed".equals(booking.getBookingStatus());
        BookingIterator iterator = new BookingIterator(bookings, completedFilter);

        int count = 0;
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            assertEquals("Completed", booking.getBookingStatus());
            count++;
        }

        assertEquals(1, count, "Should find 1 completed booking");
    }

    @Test
    public void testIteratorWithNoMatchingFilter() {
        // Test iterator with filter that matches no bookings
        Predicate<Booking> noMatchFilter = booking -> false;
        BookingIterator iterator = new BookingIterator(bookings, noMatchFilter);

        assertFalse(iterator.hasNext(), "Should have no elements");
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testIteratorWithEmptyList() {
        // Test iterator with empty list
        List<Booking> emptyList = new ArrayList<>();
        BookingIterator iterator = new BookingIterator(emptyList, booking -> true);

        assertFalse(iterator.hasNext(), "Should have no elements");
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testIteratorWithNullList() {
        // Test iterator with null list
        assertThrows(NullPointerException.class, () -> new BookingIterator(null, booking -> true));
    }

    @Test
    public void testIteratorWithNullFilter() {
        // Test iterator with null filter
        assertThrows(NullPointerException.class, () -> new BookingIterator(bookings, null));
    }

    @Test
    public void testIteratorWithComplexFilter() {
        // Test iterator with complex filter (active and pending payment)
        Predicate<Booking> complexFilter = booking ->
                "Active".equals(booking.getBookingStatus()) &&
                        "Pending".equals(booking.getPaymentStatus());

        BookingIterator iterator = new BookingIterator(bookings, complexFilter);

        int count = 0;
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            assertEquals("Active", booking.getBookingStatus());
            assertEquals("Pending", booking.getPaymentStatus());
            count++;
        }

        assertEquals(2, count, "Should find 2 active bookings with pending payment");
    }

    @Test
    public void testIteratorWithMultipleNextCalls() {
        // Test multiple next() calls without hasNext() checks
        BookingIterator iterator = new BookingIterator(bookings, booking -> true);

        // First call should succeed
        assertNotNull(iterator.next());

        // Second call should succeed
        assertNotNull(iterator.next());

        // Third call should succeed
        assertNotNull(iterator.next());

        // Fourth call should succeed
        assertNotNull(iterator.next());

        // Fifth call should succeed
        assertNotNull(iterator.next());

        // Sixth call should throw exception
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
}