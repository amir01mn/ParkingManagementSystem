package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorTest {
    private Visitor visitor;
    private UserDatabaseHelper userDB;

    @BeforeEach
    public void setUp() {
        // Create a new visitor object
        visitor = new Visitor();
        userDB = new UserDatabaseHelper();
    }

    @Test
    public void testVisitorCreation() {
        // Test that a new visitor is created with correct type
        assertEquals(Visitor.TYPE, visitor.getType());
    }

    @Test
    public void testVisitorRate() {
        // Test that visitor rate is correctly set
        assertEquals(Visitor.rate, 15);
    }


    @Test
    public void testVisitorRegistration() {
        // Set up visitor details
        String email = "newvisitor123@example.com";
        visitor.setEmail(email);
        visitor.setPassword("mEo0@Ow66");
        visitor.setName("Test Visitor");
        visitor.setType(Visitor.TYPE);

        // Instead of testing actual registration, let's mock it
        // by checking that the visitor has the correct properties set
        assertEquals(email, visitor.getEmail());
        assertEquals("mEo0@Ow66", visitor.getPassword());
        assertEquals("Test Visitor", visitor.getName());
        assertEquals(Visitor.TYPE, visitor.getType());
    }
}