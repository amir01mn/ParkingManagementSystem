package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;

public class UserDatabaseHelper {
    private static final String USER_CSV = "data/User_Database.csv";
    private static final String DELIMITER = ",";

    private static String getAbsolutePath() {
        return Paths.get(System.getProperty("user.dir"), USER_CSV).toString();
    }

    /**
     * Retrieves a user by email from the database.
     * @param email the email of the user to retrieve
     * @return the user object if found, null otherwise
     * @throws IOException if an error occurs while reading the file
     * this method is used for the registration process. It checks if the email is already registered.
     */
    public static User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(getAbsolutePath()))) {
            String line;
            boolean isFirstRow = true;

            while ((line = reader.readLine()) != null) {
                
                // Skip header row
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] data = line.split(DELIMITER);
                if (data.length >= 6 && data[1].equals(email)) {
                    User user = UserFactory.createUser(data[5]); // Use UserFactory to create the appropriate user type
                    user.setUserID(Integer.parseInt(data[0]));
                    user.setEmail(data[1]);
                    user.setPassword(data[2]);
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + getAbsolutePath() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null; // User not found
    }

    /**
     * Reads all users from the database while preserving the header row
     */
    private static List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getAbsolutePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading " + getAbsolutePath() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Writes all lines back to the file
     */
    private static boolean writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getAbsolutePath()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to " + getAbsolutePath() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user's password in the database while preserving all other data
     */
    public boolean updateUserPassword(String email, String newPassword) {
        List<String> lines = readAllLines();
        boolean updated = false;
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] data = line.split(DELIMITER);
            
            // Skip header row and check if this is the user we want to update
            if (i > 0 && data.length >= 2 && data[1].equals(email)) {
                // Preserve all existing data, only update the password
                data[2] = newPassword;
                lines.set(i, String.join(DELIMITER, data));
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return writeAllLines(lines);
        }
        return false;
    }

    /**
     * Adds a new user to the database while preserving existing data
     */
    public static boolean addUser(User user) {
        if (isEmailRegistered(user.getEmail())) {
            System.out.println("Email is already registered.");
            return false;
        }

        List<String> lines = readAllLines();
        String record = String.join(DELIMITER,
                Integer.toString(user.getUserID()),
                user.getEmail(),
                user.getPassword(),
                user.getName().substring(0, user.getName().indexOf(" ")),
                user.getName().substring(user.getName().indexOf(" ") + 1),
                user.getType(),
                "TBD"
        );
        
        lines.add(record);
        return writeAllLines(lines);
    }

    /**
     * Checks if an email is already registered.
     */
    public static boolean isEmailRegistered(String email) {
        return getUserByEmail(email) != null;
    }


    /**
     * Gets the highest existing user ID from the database
     * @return the highest user ID currently in use, or 1000 if no users exist
     */
    public static int getLastUserID() {
        List<String> lines = readAllLines();
        int maxID = 1000; // Default starting point
        boolean isFirstRow = true; // Skip header row

        for (String line : lines) {
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            String[] data = line.split(DELIMITER);
            if (data.length > 0) {
                try {
                    int id = Integer.parseInt(data[0]);
                    maxID = Math.max(maxID, id);
                }
                catch (NumberFormatException e) {
                    System.err.println("Warning: Invalid user ID format: " + data[0]);
                }
            }
        }
        return maxID;
    }
}
