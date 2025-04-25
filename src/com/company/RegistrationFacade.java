package com.company;

public interface RegistrationFacade {

    /**
     * Registers a new user
     */
    void registerUser(User user);

    /**
     * Forgets a password
     */
    void forgotPassword(String email);
}
