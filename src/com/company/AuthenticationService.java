package com.company;

import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class AuthenticationService {

    protected UserDatabaseHelper userData; // Simulated user database

    /**
     * Constructor for the AuthenticationService class
     * @param userData the user database helper
     */
    public AuthenticationService(UserDatabaseHelper userData) {

        this.userData = userData;
    }
    

    /**
     * Authenticates a user based on their email and password
     * @param email the email of the user
     * @param password the password of the user (raw, unhashed)
     * @return true if the authentication is successful, false otherwise
     */
    public boolean authenticateUser(String email, String password) {
        
        // check if the email and password are not null
        if (email == null || password == null)
            return false;
            
        User storedUser = userData.getUserByEmail(email);
        
        // check if the user is not found
        if (storedUser == null)
            return false;
 
        // Get the stored password (which is hashed)
        String storedHashedPassword = storedUser.getPassword();
        
        try {
            // Extract the salt from the stored password hash
            // The first 32 characters (16 bytes) of the hex string represent the salt
            byte[] saltBytes = new byte[16];
            for (int i = 0; i < 16; i++) {
                String byteHex = storedHashedPassword.substring(i * 2, i * 2 + 2);
                saltBytes[i] = (byte) Integer.parseInt(byteHex, 16);
            }
            
            // Hash the input password with the extracted salt
            SecureRandom random = new SecureRandom();
            int iterations = 10000;
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // Combine the salt and the new hash
            byte[] combined = new byte[saltBytes.length + hash.length];
            System.arraycopy(saltBytes, 0, combined, 0, saltBytes.length);
            System.arraycopy(hash, 0, combined, saltBytes.length, hash.length);
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : combined) {
                hexString.append(String.format("%02x", b));
            }
            
            // Compare the generated hash with the stored hash
            return storedHashedPassword.equals(hexString.toString());
            
        } catch (Exception e) {
            // If there's an error in hash comparison, return false
            System.err.println("Error comparing passwords: " + e.getMessage());
            return false;
        }
    }

   
    /**
     * Validates an email address
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public boolean validateEmail(String email) {

        // check if the email is not null
        if (email == null)
            return false;

        // check if the email is valid
        String emailRegex = "^[\\p{L}0-9+_.-]+@([\\p{L}0-9.-]+\\.[\\p{L}]{2,})$";
        
        return Pattern.matches(emailRegex, email);
    }


    /**
     * Hashes a password using PBKDF2 with SHA-256
     * @param password the password to hash
     * @return the hashed password with salt
     */
    public String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Use PBKDF2 with SHA-256
            int iterations = 10000;
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // Combine salt and hash
            byte[] combined = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : combined) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }
}
