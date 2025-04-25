package com.company;

import java.time.LocalTime;

public interface BookingFacade {
    void cancelBooking();
    void extendBooking(String bookingID, LocalTime end);
    void payDeposit(int user);
    void checkout();
}
