package com.company;

import java.time.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;

public class Payment {

	private String paymentID;
	private double amount;
	private static double deposit;
	private String method;
	private PriceCalculator calculator;
	private boolean paymentStatus = false;
	public Booking booking;
	private static final String PAYMENT_CSV = "Payment_Database.csv";
	private static final String DELIMITER = ",";

	private static String getAbsolutePath(String filename) {
        return Paths.get(System.getProperty("user.dir"), "data", filename).toString();
    }

	public Payment(String paymentID, double amount, double deposit, String method, Booking booking) {

		this.paymentID = paymentID;
		this.setAmount(amount);
		this.setDeposit(deposit);
		this.method = method;
		this.booking = booking;
	}


	public String getPaymDetails() {

		return "PaymentID: " + getPaymentID()  +
				", Amount: $" + getAmount() +
				", Deposit: $" + getDeposit() +
				", Method: " + getMethod();
	}



	public double getAmount() {

		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}


	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public String getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}


	public PriceCalculator getCalculator() {
		return calculator;
	}

	public void setCalculator(PriceCalculator calculator) {
		this.calculator = calculator;
	}

	/**
	 * Return whether the payment is successful or not. (Assume that the payment is successful)
	 * This method can changed if we would have access to a payment gateway
	 * @return the payment status
	 */
	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}



	/**
	 * Process the deposit payment
	 * We assume that the user payment is successful
	 * @param userID the user ID of the user
	 * @return true if the deposit is processed, false otherwise
	 */
	public boolean processDepositPayment(int userID) {

		String userType = calculator.getUserType(userID); // Retrieve user type
		
		// Return false if user type is null (invalid user)
		if (userType == null) {
			System.out.println("Invalid user ID: " + userID);
			return false;
		}
		
		double depositAmount = calculator.checkRate(userType); // Get deposit based on user type

		if (depositAmount > 0) {

			this.deposit = depositAmount;
			System.out.println("Processing deposit payment of $" + depositAmount + " for user type: " + userType);
			this.paymentStatus = true; // Mark payment as successful
			savePaymentToDatabase(userID, depositAmount); // Save the payment to the database
			return true;
		}
		else {
			System.out.println("Error occurred while processing your deposit. Please try again later");
			return false;
		}
	}

	/**
	 * Saves payment details to Payment_Database.csv.
	 * @param userID the user ID of the user
	 * @param depositAmount the deposit amount
	 */
	private void savePaymentToDatabase(int userID, double depositAmount) {
		try {
			// Create the data directory if it doesn't exist
			File dataDir = new File("data");
			if (!dataDir.exists()) {
				dataDir.mkdirs();
			}

			// Write the payment details to the csv file
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAbsolutePath(PAYMENT_CSV), true))) {
				String record = String.join(DELIMITER,
						paymentID,
						String.valueOf(userID),
						method,
						String.valueOf(depositAmount)
				);
				writer.write(record);
				writer.newLine();
				System.out.println("Payment record saved to database.");
			}
		} catch (IOException e) {
			System.err.println("Error saving payment record: " + e.getMessage());
		}
	}

	/**
	 * Process the payment. Assume that the payment is successful
	 * @param deposit the deposit amount
	 * @return true if the payment is processed, false otherwise
	 */
	public boolean processPayment(double deposit) {

		System.out.println("Processing payment for the amount of " + deposit);
		return paymentStatus;
	}

	/**
	 * Charge the second payment. Assume that the payment is done automatically and successfully
	 * @param bookingID the booking ID
	 */
	public void chargeSecondPayment(String bookingID) {

		double secondPayment = calculator.calculateSecondPayment(amount, deposit);
		processPayment(secondPayment);
		verifyPaymentStatus();
		System.out.println("Charging second payment for booking: " + bookingID);
	}


	/**
	 * Cancels a booking and determines whether a refund should be processed.
	 * @param bookingID the booking ID
	 */
	public static void cancelBooking(String bookingID) {

		Booking booking = BookingDatabaseHelper.findBookingByID(bookingID); //extract the booking

		if (booking == null) {
			System.out.println("Booking not found. Cancellation failed.");
			return;
		}

		// Extract the start time from the booking
		LocalTime bookingStartTime = booking.getStart();
		LocalTime currentTime = LocalTime.now();

		// If the current time is before the start time of the booking, then process the refund
		if (currentTime.isBefore(bookingStartTime)) {

			System.out.println("Cancellation successful. Processing refund...");
			refundDeposit(deposit);
		}
		else // If the cancellation is made after the start time of the booking
			System.out.println("Cancellation successful. Refund was not issued as the cancellation has been made within 1 hour of start time.");

		BookingDatabaseHelper.cancelBooking(bookingID);
	}

	/**
	 * Refund the deposit
	 * @param deposit the deposit amount
	 * @return true if the deposit is refunded, false otherwise
	 */
	public static boolean refundDeposit(double deposit) {

		System.out.println("Refunded deposit of $" + deposit);
		return true;
	}

	/**
	 * Return whether the payment is successful or not
	 * @return the payment status
	 */
	public String verifyPaymentStatus() {

		return paymentStatus ? "Payment Successful" : "Payment Failed";
	}
}
