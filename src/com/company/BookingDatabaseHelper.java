package com.company;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BookingDatabaseHelper {

    private static final String BOOKING_CSV = "data/Booking_Database.csv";
    private static final String DELIMITER = ",";

    static String getAbsolutePath() {
        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, BOOKING_CSV).toString();
    }

    /**
     * Reads all lines from the booking database file
     */
    static List<String> readAllLines() {

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getAbsolutePath()))) {

            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);

        }
        catch (IOException e) {
            System.err.println("Error reading booking database: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Writes all lines back to the booking database file
     */
    static boolean writeAllLines(List<String> lines) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAbsolutePath()))) {

            for (String line : lines) {

                writer.write(line);
                writer.newLine();
            }

            return true;
        }
        catch (IOException e) {
            System.err.println("Error writing to booking database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a specific field in a booking record
     */
    private static void updateBookingField(String bookingID, int fieldIndex, String newValue) {

        List<String> lines = readAllLines();
        if (lines.isEmpty()) return;

        // Find and update the specific line
        for (int i = 1; i < lines.size(); i++) {

            // Start from 1 to skip header
            String[] data = lines.get(i).split(DELIMITER);
            if (data.length > 0 && data[0].equals(bookingID)) {

                data[fieldIndex] = newValue;
                lines.set(i, String.join(DELIMITER, data));
                break;
            }
        }

        writeAllLines(lines);
    }

    /**
     * Saves a new booking to the database
     */
    public static void saveBooking(Booking booking) {

        // Validate required fields
        if (booking == null)
            throw new IllegalArgumentException("Booking cannot be null");

        if (booking.getBookingID() == null)
            throw new IllegalArgumentException("Booking ID cannot be null");

        if (booking.getPlate() == null)
            throw new IllegalArgumentException("Plate number cannot be null");

        if (booking.getStart() == null)
            throw new IllegalArgumentException("Start time cannot be null");

        if (booking.getEnd() == null)
            throw new IllegalArgumentException("End time cannot be null");

        if (booking.getBookingStatus() == null)
            throw new IllegalArgumentException("Booking status cannot be null");

        if (booking.getPaymentStatus() == null)
            throw new IllegalArgumentException("Payment status cannot be null");

        // Add notification observer
        UserNotificationService notificationService = new UserNotificationService();
        booking.addObserver(notificationService);

        List<String> lines = readAllLines();

        if (lines.isEmpty())
            return;

        // Check for duplicate booking ID
        for (int i = 1; i < lines.size(); i++) { // Start from 1 to skip header

            String line = lines.get(i);
            String[] data = line.split(DELIMITER);

            if (data.length > 0 && data[0].trim().equals(booking.getBookingID().trim()))
                throw new IllegalArgumentException("Booking ID already exists: " + booking.getBookingID());
        }

        // Add the new booking record with HH:mm format
        String newLine = String.format("%s,%d,%d,%d,%s,%s,%s,%s,%.2f,%s,%.2f",
            booking.getBookingID(),
            booking.getUserID(),
            booking.getParkingSpace(),
            booking.getParkingLot(),
            booking.getPlate(),
            booking.getStart().format(DateTimeFormatter.ofPattern("HH:mm")),
            booking.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")),
            booking.getPaymentStatus(),
            booking.getAmount(),
            booking.getBookingStatus(),
            booking.getTotalAmount()
        );
        lines.add(newLine);

        writeAllLines(lines);
    }


    public static void updateBookingStatus(String bookingID, String newStatus) {
        updateBookingField(bookingID, 9, newStatus); // Status is at index 9
    }


    public static void updatePaymentStatus(String bookingID, String newStatus) {
        updateBookingField(bookingID, 7, newStatus); // Payment status is at index 7
    }


    public static void updateTotalAmount(String bookingID, double newAmount) {
        updateBookingField(bookingID, 10, String.valueOf(newAmount)); // Total amount is at index 10
    }


    public static void updateEndTime(String bookingID, LocalTime newEndTime) {
        updateBookingField(bookingID, 6, newEndTime.format(DateTimeFormatter.ISO_LOCAL_TIME)); // End time is at index 6
    }


    public static List<Booking> readAllBookings() {

        List<Booking> bookings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getAbsolutePath()))) {

            // Skip the header line
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(DELIMITER);

                if (data.length >= 10) { // Ensure we have all required fields
                    Booking booking = convertToBooking(data);

                    if (booking != null)
                        bookings.add(booking);
                }
            }
        } 
        catch (IOException e) {
            System.err.println("Error reading bookings from file: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }



    static Booking convertToBooking(String[] data) {

        // Throw NullPointerException if input data is null
        if (data == null) {
            throw new NullPointerException("Cannot load from object array because 'data' is null");
        }

        try {

            if (data[0].equals("booking_id"))
                return null;

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]");
            
            double depositAmount = Double.parseDouble(data[8]);
            
            double totalAmount = data.length > 10 ? Double.parseDouble(data[10]) : depositAmount;
            
            return new Booking.BookingBuilder()
                .setBookingID(data[0])
                .setUserID(Integer.parseInt(data[1]))
                .setParkingSpace(Integer.parseInt(data[2]))
                .setParkingLot(Integer.parseInt(data[3]))
                .setPlate(data[4])
                .setStart(LocalTime.parse(data[5], timeFormatter))
                .setEnd(LocalTime.parse(data[6], timeFormatter))
                .setPaymentStatus(data[7])
                .setAmount(depositAmount)
                .setBookingStatus(data[9])
                .setTotalAmount(totalAmount)
                .build();

        } catch (Exception e) {

            System.err.println("Error converting data to booking: " + e.getMessage());
            if (data != null)
                System.err.println("Data causing error: " + String.join(", ", data));
            
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds a booking by its ID.
     */
    public static Booking findBookingByID(String bookingID) {

        List<Booking> bookings = readAllBookings();

        for (Booking booking : bookings) {

            if (booking.getBookingID().equals(bookingID))
                return booking;
            
        }
        return null; // Not found
    }

    /**
     * Update the status to "Cancelled" once cancelled in Payment and Booking class
     * */
    public static void cancelBooking(String bookingID) {

        updateBookingStatus(bookingID, "Cancelled");
    }


    /**
     * Returns a list of bookings that overlap with the specified time period
     * @param start The start time to check
     * @param end The end time to check
     * @return ArrayList of bookings that are active during the specified period
     */
    public static ArrayList<Booking> getBookingsForTimeSlot(LocalTime start, LocalTime end) {

        ArrayList<Booking> bookingsInTimeSlot = new ArrayList<>();
        List<Booking> allBookings = readAllBookings();

        for (Booking booking : allBookings) {
            
            // Check if the booking overlaps with the specified time period
            LocalTime bookingStart = booking.getStart();
            LocalTime bookingEnd = booking.getEnd();
            
            // Booking overlaps if it doesn't end before our start time
            // and doesn't start after our end time
            if (!(bookingEnd.isBefore(start) || bookingStart.isAfter(end))) {
                bookingsInTimeSlot.add(booking);
            }
        }

        return bookingsInTimeSlot;
    }

    /**
     * Gets the last used booking ID from the database
     * @return the highest booking ID number currently in use
     */
    public static int getLastBookingID() {
        List<Booking> bookings = readAllBookings();
        int maxID = 0;

        for (Booking booking : bookings) {

            String bookingID = booking.getBookingID();

            if (bookingID != null && bookingID.startsWith("N2S")) {
                    
                // Extract the number part from "N2S000001" format
                int idNumber = Integer.parseInt(bookingID.substring(3));
                maxID = Math.max(maxID, idNumber);
            }
        }
        return maxID;
    }
}
