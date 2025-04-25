package com.company;

public class RegisterationSystem implements RegistrationFacade {

    private UserDatabaseHelper userDB;
    private AuthenticationService authService;
    private Student student;
    private FacultyMember facultyMember;
    private NonFacultyStaff nonFacultyStaff;

    /**
     * Constructor for the RegisterationSystem class
     */
    public RegisterationSystem() {

        this.userDB = new UserDatabaseHelper();
        this.authService = new AuthenticationService(new UserDatabaseHelper());
    }

    /**
     * Internal method that handles user registration logic
     * This is used internally by registerUser() which is the public API
     * 
     * @param user the user to register
     * @return true if registration was successful, false otherwise
     */
    public boolean register(User user) {

        // check if the user is null or if the email is already registered
        if (user == null || UserDatabaseHelper.isEmailRegistered(user.getEmail()))
            return false;

        // check if the email is valid using the user's validate method
        if (!user.validate())
            return false;

        // hash the password
        String hashedPassword = authService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // register the user
        UserDatabaseHelper.addUser(user);

        System.out.println("User registered successfully.");
        return true;
    }

    /**
     * Logs in a user
     */
    public boolean loginUser(User user) {

        if (user == null || !UserDatabaseHelper.isEmailRegistered(user.getEmail()))
            return false;

        User storedUser = UserDatabaseHelper.getUserByEmail(user.getEmail());

        assert storedUser != null;
        if(!authService.authenticateUser(storedUser.getEmail(), user.getPassword())){

            forgotPassword(user.getEmail());
            return false;
        }
        return true;
    }

    /**
     * Logs out a user
     */
    public void logout(User user) {
        System.out.println(user.getEmail() + " has been logged out.");
    }

    /**
     * Validates an email
     */
    public boolean enterEmail(String email) {
        return authService.validateEmail(email);
    }

    /**
     * Sends a verification email
     */
    public boolean sendVerificationEmail(String email) {
        if (!authService.validateEmail(email)) {
            return false;
        }

        System.out.println("Verification email sent to " + email);
        return true;
    }

    /**
     * Creates an account
     * 
     * @deprecated This method is a placeholder and does nothing useful.
     * Use {@link #registerUser(User)} or {@link #register(User)} instead.
     */
    @Deprecated
    public boolean create() {
        // Placeholder for handling account creation via email verification
        return true;
    }

    /**
     * Sets a password
     */
    public void setPassword(String email, String newPassword) {

        if (!UserDatabaseHelper.isEmailRegistered(email)) {
            System.out.println("User not found.");

            // this return statement is used to stop the execution of setting a password in the database if the user is not found
            return;
        }

        String hashedPassword = authService.hashPassword(newPassword);

        if (userDB.updateUserPassword(email, hashedPassword))
            System.out.println("Password updated successfully.");
        
    }


    /**
     * Registers a user
     * This is the primary API method for registration as defined in the RegistrationFacade interface.
     * It delegates to the internal register() method for most of the implementation.
     * 
     * @param user the user to register
     */
    @Override
    public void registerUser(User user) {

        boolean success = register(user);

        if (!success) 
            System.out.println("Registration failed.");
        
    }


    /**
     * Handles the forgotten password flow
     * 
     * @param email The email address of the user who forgot their password
     */
    @Override
    public void forgotPassword(String email) {
        
        if (!UserDatabaseHelper.isEmailRegistered(email)){
            System.out.println("User not found.");

            // this return statement is used to stop the execution of setting new password in the database if the user is not found
            return;
        }

        setPassword(email, "newPassword");
    }
}