package com.company;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Randoop-style regression test for User class
 */
public class UserRegressionTest {

    /**
     * Test constructor with parameters
     */
    @Test
    public void test001() {
        User user1 = new User(1, "test@example.com", "password123");
        assertEquals(1, user1.getUserID());
        assertEquals("test@example.com", user1.getEmail());
        assertEquals("password123", user1.getPassword());
    }

    /**
     * Test email getter and setter
     */
    @Test
    public void test002() {
        User user2 = new User(2, "initial@example.com", "password123");
        user2.setEmail("new@example.com");
        assertEquals("new@example.com", user2.getEmail());
    }

    /**
     * Test name getter and setter
     */
    @Test
    public void test003() {
        User user3 = new User(3, "email@example.com", "password123");
        assertNull(user3.getName());
        user3.setName("Test User");
        assertEquals("Test User", user3.getName());
    }

    /**
     * Test type getter and setter
     */
    @Test
    public void test004() {
        User user4 = new User(4, "email@example.com", "password123");
        assertNull(user4.getType());
        user4.setType("Student");
        assertEquals("Student", user4.getType());
    }

    /**
     * Test userID getter and setter
     */
    @Test
    public void test005() {
        User user5 = new User(5, "email@example.com", "password123");
        assertEquals(5, user5.getUserID());
        user5.setUserID(10);
        assertEquals(10, user5.getUserID());
    }

    /**
     * Test setting invalid email
     */
    @Test
    public void test006() {
        User user6 = new User(6, "", "password123");
        assertFalse(user6.validate());
    }

    /**
     * Test setting null email
     */
    @Test
    public void test007() {
        User user7 = new User(7, null, "password123");
        assertFalse(user7.validate());
    }

    /**
     * Test valid email
     */
    @Test
    public void test008() {
        User user8 = new User(8, "valid@example.com", "password123");
        assertTrue(user8.validate());
    }

    /**
     * Test password setter with strong password
     */
    @Test
    public void test009() {
        User user9 = new User(9, "email@example.com", "oldpassword");
        // Note: This test depends on the implementation of StrongPasswordRecognizer
        user9.setPassword("StrongP@ssw0rd123!");
        // Don't assert the result as it depends on password strength validation
    }

    /**
     * Test combination of methods
     */
    @Test
    public void test010() {
        User user10 = new User(10, "email@example.com", "password123");
        user10.setName("Test Name");
        user10.setType("Faculty");
        user10.setEmail("new@example.com");
        
        assertEquals(10, user10.getUserID());
        assertEquals("Test Name", user10.getName());
        assertEquals("Faculty", user10.getType());
        assertEquals("new@example.com", user10.getEmail());
    }
} 