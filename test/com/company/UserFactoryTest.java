package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserFactoryTest {

	@Test
    public void testCreateStudent() {
        User user = UserFactory.createUser("Student");
        assertTrue(user instanceof Student);
        assertEquals("Student", user.getType());
    }

    @Test
    public void testCreateFaculty() {
        User user = UserFactory.createUser("Faculty Member");
        assertTrue(user instanceof FacultyMember);
        assertEquals("Faculty Member", user.getType());
    }

    @Test
    public void testCreateNonFacultyStaff() {
        User user = UserFactory.createUser("Non-Faculty Staff");
        assertTrue(user instanceof NonFacultyStaff);
        assertEquals("Non-Faculty Staff", user.getType());
    }

    @Test
    public void testCreateUnknownTypeReturnsVisitor() {
        User user = UserFactory.createUser("Alien");
        assertTrue(user instanceof Visitor);
        assertEquals("Visitor", user.getType());
    }

    @Test
    public void testCreateUserNullType() {
        User user = UserFactory.createUser(null);
        assertTrue(user instanceof Visitor);
        assertEquals("Visitor", user.getType());
    }

    @Test
    public void testCreateUserWithLowerCaseType() {
        // Test case to check case sensitivity for "Student"
        User user = UserFactory.createUser("Student");
        assertTrue(user instanceof Student);
        assertEquals("Student", user.getType());
    }

    @Test
    public void testCreateUserWithEmptyString() {
        // Test case to check behavior when an empty string is passed
        User user = UserFactory.createUser("");
        assertTrue(user instanceof Visitor);
        assertEquals("Visitor", user.getType());
    }
}
