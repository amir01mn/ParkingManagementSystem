package com.company;

public class User {

    private String name;
    private String email;
    private String type;
    private int userID;
    private String password;


    public User(){

        synchronized(User.class) {
            this.userID = UserDatabaseHelper.getLastUserID() + 1;
        }
    }

    /**
     * Constructor for User class.
     * @param userId the user ID of the user
     */
    public User(int userId) {

        this.userID = userId;
        // Other fields can be loaded from UserDatabaseHelper as well if we want to
    }

    /**
     * Constructor for User class.
     * @param id the user ID of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(int id, String email, String password) {

        this.userID = id;
        this.email = email;
        this.password = password;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }


    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public int getUserID() {

        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        if (StrongPasswordRecognizer.isPasswordStrong(password))
            this.password = password;
            
        else
            System.out.println("Password is not strong enough");    
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Validates the user's data.
     * @return true if the user's data is valid, false otherwise
     */
    public boolean validate() {
        // Default validation - can be overridden by subclasses
        return email != null && !email.trim().isEmpty();
    }
}
