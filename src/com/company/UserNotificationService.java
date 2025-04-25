package com.company;

public class UserNotificationService implements BookingOberver{

    //what should I do
    @Override
    public void update(Booking booking) {

        System.out.println("Notification: Booking " + booking.getBookingID() + " status changed to " + booking.getStatus());
    }
}
