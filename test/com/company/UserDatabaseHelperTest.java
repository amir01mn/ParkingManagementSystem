package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserDatabaseHelperTest {

	@Test
    public void testGetUserByEmailTrue() {
        User user = UserDatabaseHelper.getUserByEmail("staff3@example.com");
        assertNotNull(user);
        assertEquals("staff3@example.com", user.getEmail());
    }

	@Test
    public void testGetUserByEmailFalse() {

        User user = UserDatabaseHelper.getUserByEmail("nonexistent@email.com");
        assertNull(user);
    }

	@Test
    public void testAddUserSuccess() {
        User user = new User(1013, "student8@email.com", "Strong123!");
        user.setName("John Dorie");
        user.setType("Student");
        UserDatabaseHelper.addUser(user);
        assertTrue(UserDatabaseHelper.isEmailRegistered("student8@email.com"));
    }

    @Test
    public void testAddUserFail() {
        User user = new User(101, "visitor1@example.com", "Strong123!");
        user.setName("Melly Mel");
        user.setType("Faculty");

        assertFalse(UserDatabaseHelper.addUser(user));
    }

    @Test
    public void testUpdateUserPasswordSuccess() {
        UserDatabaseHelper db = new UserDatabaseHelper();
        boolean result = db.updateUserPassword("visitor3@example.com", "NewPass123!");
        assertTrue(result);
    }

    @Test
    public void testUpdateUserPasswordFail() {
        UserDatabaseHelper db = new UserDatabaseHelper();
        boolean result = db.updateUserPassword("fake@email.com", "NewPass123!");
        assertFalse(result);
    }

    @Test
	 public void testIsEmailRegisteredTrue() {
		 assertTrue(UserDatabaseHelper.isEmailRegistered("visitor3@example.com"));
	}

    @Test
	public void testIsEmailRegisteredFalse() {
		assertFalse(UserDatabaseHelper.isEmailRegistered("fake@email.com"));
	}

	@Test
    public void testGetLastUserID() {
        int lastID = UserDatabaseHelper.getLastUserID();
        assertTrue(lastID >= 1);
    }
}