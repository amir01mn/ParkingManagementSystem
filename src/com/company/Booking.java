package com.company;

import java.time.LocalTime;
import java.util.*;
import java.util.ArrayList;

/**
 * Requires DB actions
 */
public class Booking implements BookingFacade {

    private String bookingID;
    private int userID;
    private int parkingSpaceID;
    private int parkingLotID;
    private LocalTime start;
    private LocalTime end;
    private String bookingStatus;
    private String paymentStatus;
    private String plate;
    private List<BookingOberver> observers = new ArrayList<>();
    private Payment payment;
    private double amount; //deposit amount
    private double totalAmount;
    private static int lastGeneratedID = 0;
    private PriceCalculator priceCalculator;

    private Booking(BookingBuilder builder) {

        this.bookingID = builder.bookingID;
        this.userID = builder.userID;
        this.parkingSpaceID = builder.parkingSpaceID;
        this.parkingLotID = builder.parkingLotID;
        this.start = builder.start;
        this.end = builder.end;
        this.bookingStatus = builder.bookingStatus;
        this.plate = builder.plate;
        this.paymentStatus = builder.paymentStatus;
        this.amount = builder.amount;
        this.totalAmount = builder.totalAmount;
        this.priceCalculator = new PriceCalculator();
    }


    /**
     * Generates a unique booking ID
     *
     * @return String representation of the unique booking ID
     */
    static synchronized String generateUniqueID() {

        // Read the last ID from database first
        int lastIDFromDB = BookingDatabaseHelper.getLastBookingID();
        // Use the larger of lastGeneratedID and lastIDFromDB to ensure uniqueness
        lastGeneratedID = Math.max(lastGeneratedID, lastIDFromDB) + 1;
        return String.format("N2S%06d", lastGeneratedID); // Creates IDs like N2S000001, N2S000002, etc.
    }


    public String getBookingID() {

        return bookingID;
    }


    public int getUserID() {

        return userID;
    }


    public int getParkingLot() {

        return parkingLotID;
    }


    public int getParkingSpace() {

        return parkingSpaceID;
    }


    public LocalTime getStart() {

        return start;
    }


    public LocalTime getEnd() {

        return end;
    }


    public String getStatus() {

        return bookingStatus;
    }


    public String getPlate() {

        return plate;
    }

    public String getBookingStatus() {

        return bookingStatus;
    }

    public String getPaymentStatus() {

        return paymentStatus;
    }

    public double getAmount() {

        return amount;
    }


    public double getTotalAmount() {
        return totalAmount;
    }


    public void saveBooking() {

        BookingDatabaseHelper.saveBooking(this);
    }

    public void cancelBooking() {

        this.bookingStatus = "Cancelled";
        BookingDatabaseHelper.updateBookingStatus(bookingID, "Cancelled");
        Payment.cancelBooking(bookingID);
        notifyObservers();
    }

    // assume all plates are verified except "INVALID"
    public static boolean verifyLicencePlate(String plate) throws IllegalArgumentException {

        if (plate == "INVALID")
            throw new IllegalArgumentException();
        return !Objects.equals(plate, "INVALID");
    }

    @Override
    public void extendBooking(String bookingID, LocalTime newEnd) {

        try {
            if (newEnd.isAfter(this.end)) {

                // Calculate the time difference in hours
                long hours = java.time.Duration.between(this.end, newEnd).toHours();
                double rate = priceCalculator.checkRate(priceCalculator.getUserType(userID));
                double extraCharge = rate * hours;

                this.end = newEnd;
                this.totalAmount += extraCharge;

                // Update the booking in the database
                BookingDatabaseHelper.updateEndTime(bookingID, newEnd);
                BookingDatabaseHelper.updateTotalAmount(bookingID, this.totalAmount);
                this.saveBooking(); // Save the updated booking

                System.out.println("Booking extended. Additional charge: $" + extraCharge);
            }
            else
                System.out.println("Invalid time. End time must be later than the current end time.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public void payDeposit(int userID) {

        if (payment == null) {

            System.out.println("Payment not initialized");
            return;
        }

        boolean result = payment.processDepositPayment(userID);
        this.amount = payment.getAmount();

        if (result) {

            this.paymentStatus = "Paid";
            BookingDatabaseHelper.updatePaymentStatus(bookingID, "Paid");
            System.out.println("Deposit payment successful.");
        }
        else
            System.out.println("Deposit payment failed.");
    }


    public void addObserver(BookingOberver observer) {

        observers.add(observer);
    }

    void notifyObservers() {

        for (BookingOberver observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void checkout() {

        if (LocalTime.now().isAfter(end)) {

            if ("Paid".equals(paymentStatus)) {
                
                if (payment != null) {
                    double secondPayment = totalAmount - amount;

                    if (secondPayment > 0) {

                        payment.chargeSecondPayment(bookingID);
                        this.paymentStatus = "Completed";
                        BookingDatabaseHelper.updatePaymentStatus(bookingID, "Completed");

                        System.out.println("Second payment processed successfully. Total amount paid: $" + totalAmount);
                    } else {
                        // If deposit covers full amount, still mark as completed
                        this.paymentStatus = "Completed";
                        BookingDatabaseHelper.updatePaymentStatus(bookingID, "Completed");
                        System.out.println("No second payment required. Deposit covers the full amount.");
                    }

                } else 
                    System.out.println("Payment not initialized for booking " + bookingID);

            } else
                System.out.println("Cannot process checkout. Deposit payment not completed.");

        } else
            System.out.println("Cannot process checkout. Booking has not ended yet.");

    }

    static class BookingBuilder {

        private String bookingID;
        private int userID;
        private int parkingSpaceID;
        private int parkingLotID;
        private LocalTime start;
        private LocalTime end;
        private String bookingStatus;
        private String plate;
        private String paymentStatus;
        private double amount;
        private double totalAmount;

        public BookingBuilder() {

            // Set default values
            this.bookingID = null;  // Don't generate ID automatically
            this.userID = 0;
            this.parkingSpaceID = 0;
            this.parkingLotID = 0;
            this.start = LocalTime.now();
            this.end = LocalTime.now().plusHours(1);
            this.bookingStatus = "Active";
            this.plate = "";
            this.paymentStatus = "Pending";
            this.amount = 0.0;
            this.totalAmount = 0.0;
        }

        public BookingBuilder setBookingID(String bookingID) {
            this.bookingID = bookingID;
            return this;
        }


        public BookingBuilder setUserID(int userID) {

            this.userID = userID;
            return this;
        }


        public BookingBuilder setParkingSpace(int parkingSpaceID) {

            this.parkingSpaceID = parkingSpaceID;
            return this;
        }


        public BookingBuilder setParkingLot(int parkingLotID) {

            this.parkingLotID = parkingLotID;
            return this;
        }


        public BookingBuilder setStart(LocalTime start) {

            this.start = start;
            return this;
        }


        public BookingBuilder setEnd(LocalTime end) {

            this.end = end;
            return this;
        }


        public BookingBuilder setBookingStatus(String bookingStatus) {

            this.bookingStatus = bookingStatus;
            return this;
        }

        public BookingBuilder setPlate(String plate) throws Exception {

            if (verifyLicencePlate(plate)) {
                this.plate = plate;
                 return this;
            }
            return null;
        }

        public BookingBuilder setPaymentStatus(String paymentStatus) {

            this.paymentStatus = paymentStatus;
            return this;
        }

        public BookingBuilder setAmount(double amount) {

            this.amount = amount;
            return this;
        }


        public BookingBuilder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Booking build() {

            return new Booking(this);
        }
    }
}