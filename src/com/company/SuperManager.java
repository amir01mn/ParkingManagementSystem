package com.company;

import java.io.*;
import java.util.*;

public class SuperManager extends Manager{

	private static final String MANAGER_CSV = "Manager_Database.csv";
	private static SuperManager superManager;
	
	public SuperManager(String name, String email, String password) {
		super(name, email, password);
	}

	public static SuperManager getInstance() {
        
		if (superManager == null) {
        	superManager = new SuperManager("SuperManager", "super@parking.com", "2/d/rC8y05&s!");
        }
        return superManager;
    }
	
	/**
	 * Public method to create a manager account
	 * Assuming it always creates an account successfully
	 * @return true if account creation was successful, false otherwise
	 */
	public boolean createManagerAccount() {
		return autoAccountGenerator();
	}
	
	private String generateSecurePassword() {
	    
		String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowercase = "abcdefghijklmnopqrstuvwxyz";
		String digits = "0123456789";
		String special = "!@#$%^&*()";
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();

		// Ensure at least one character from each category
		sb.append(uppercase.charAt(rand.nextInt(uppercase.length())));
		sb.append(lowercase.charAt(rand.nextInt(lowercase.length())));
		sb.append(digits.charAt(rand.nextInt(digits.length())));
		sb.append(special.charAt(rand.nextInt(special.length())));

		// Add more random characters to make it longer (total of 12)
		String allChars = uppercase + lowercase + digits + special;
		for (int i = 0; i < 8; i++) {
			sb.append(allChars.charAt(rand.nextInt(allChars.length())));
		}

		// Shuffle the characters to make it more random
		char[] passwordArray = sb.toString().toCharArray();
		for (int i = 0; i < passwordArray.length; i++) {
			int j = rand.nextInt(passwordArray.length);
			char temp = passwordArray[i];
			passwordArray[i] = passwordArray[j];
			passwordArray[j] = temp;
		}

		return new String(passwordArray);
	}
	
	private int getNextManagerID(String file) {
	    
		int lastID = 0;

	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        
	    	String line;
	        br.readLine();

	        while ((line = br.readLine()) != null) {
	            
	        	String[] data = line.split(",");
	            
	        	if (data.length > 0) {
	                try {
	                    int id = Integer.parseInt(data[0].trim());
	                    if (id > lastID) lastID = id;
	                } 
	                catch (NumberFormatException e) {}
	            }
	        }
	    } 
	    catch (IOException e) {
	    	
	    }

	    return lastID + 1;
	}

	
	private boolean autoAccountGenerator() {
		
		if (this != getInstance()) {
	        
			System.out.println("No access, only super manager can generate accounts.");
	        return false;
	    }

	    try {
	        int id = getNextManagerID(MANAGER_CSV);
	        String name = "Admin" + id;
	        String password = generateSecurePassword();

	        String newRow = id + "," + name + "," + password + ",FALSE";
	        FileWriter fw = new FileWriter(MANAGER_CSV, true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        PrintWriter pw = new PrintWriter(bw);
	        
	        pw.println();
	        pw.println(newRow);
	        pw.close();

	        System.out.println("Manager account created: " + name);
	        return true;

	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
