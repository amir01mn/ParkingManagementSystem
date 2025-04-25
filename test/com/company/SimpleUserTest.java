package com.company;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUserTest {

    @Test
    public void testUserConstruction() {
        User user = new User(1, "johndoe@example.com", "password123");
        
        assertEquals(1, user.getUserID());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    public void testUserGettersAndSetters() {
        User user = new User(1, "johndoe@example.com", "password123");
        
        user.setName("John Doe");
        assertEquals("John Doe", user.getName());
        
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
        
        user.setType("Student");
        assertEquals("Student", user.getType());
        
        // For password, use a direct setter test that doesn't depend on validation
        user.setUserID(2);
        assertEquals(2, user.getUserID());
    }
    
    @Test
    public void testValidate() {
        User user = new User(1, "johndoe@example.com", "password123");
        assertTrue(user.validate());
        
        User userWithEmptyEmail = new User(2, "", "password123");
        assertFalse(userWithEmptyEmail.validate());
        
        User userWithNullEmail = new User(3, null, "password123");
        assertFalse(userWithNullEmail.validate());
    }
} 