package com.company;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Paths;

public class PriceCalculator {
	
	private static final String USER_CSV = "User_Database.csv";
	private static final String BOOKING_CSV = "Booking_Database.csv";
    
    private double amount;
    private double deposit;
    
    private static final Map<String, Double> hourlyRates = new HashMap<>();
    
    static {
        hourlyRates.put("Student", 5.00);
        hourlyRates.put("Faculty", 8.00);
        hourlyRates.put("Non-Faculty Staff", 10.00);
        hourlyRates.put("Visitor", 15.00);
    }
    
    private static String getAbsolutePath(String filename) {
        return Paths.get(System.getProperty("user.dir"), "data", filename).toString();
    }
    
    public PriceCalculator() {
    	this.amount = 0.00;
    	this.deposit = 0.00;
    }
    
    public String getUserType(int userID) {
    	
    	String user_type = null; // Return null for invalid users
        
    	try (BufferedReader br = new BufferedReader(new FileReader(getAbsolutePath(USER_CSV)))) {
    		
    		String line;
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length >= 6 && Integer.parseInt(data[0].trim()) == userID) {
                    user_type = data[5].trim();
                    break;
                }
            }
        } 
        catch (IOException e) {
            // If file doesn't exist or can't be read, return null
            System.err.println("Warning: Could not read user database at: " + getAbsolutePath(USER_CSV));
        }
        return user_type;
    }


    public double checkRate(String user_type) {
        // Use a default rate of 15.00 for null or unknown user types
        if (user_type == null) {
            this.deposit = 15.00; // Default visitor rate
            return deposit;
        }
        
        this.deposit = hourlyRates.getOrDefault(user_type, 15.00); // default is visitor
        return deposit;
    }
    
    public double calculateTotalPrice(int userID, LocalDateTime start, LocalDateTime end) {
        // Handle case when start and end are in the wrong order (negative hours)
        if (start.isAfter(end)) {
            // Swap start and end to ensure positive duration
            LocalDateTime temp = start;
            start = end;
            end = temp;
        }
        
        long time = Duration.between(start, end).toHours();
        // Ensure time is not negative
        time = Math.max(0, time);
        
        double rate = checkRate(getUserType(userID));
        
        this.amount = rate * time;
        
        return this.amount;
    }
    
    public double calculateTotalPayment(int userID, LocalDateTime start, LocalDateTime end, double deposit) {
        this.deposit = deposit;
        // Fix the parameter order - it should be start, end not end, start
        double total = calculateTotalPrice(userID, start, end);
        return total;
    }
    
    public double calculateSecondPayment(double total_price, double deposit) {
        
    	return total_price - deposit;
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
    
}
