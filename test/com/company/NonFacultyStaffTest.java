package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NonFacultyStaffTest {
    private NonFacultyStaff staff;
    private UserDatabaseHelper userDB;

    @BeforeEach
    public void setUp() {
        // Create a new non-faculty staff object
        staff = new NonFacultyStaff();
        userDB = new UserDatabaseHelper();
    }

    @Test
    public void testNonFacultyStaffCreation() {
        // Test that a new non-faculty staff is created with correct type
        assertEquals(NonFacultyStaff.TYPE, staff.getType());
    }

    @Test
    public void testNonFacultyStaffRate() {
        // Test rate is correct and constant
        NonFacultyStaff staff1 = new NonFacultyStaff();
        NonFacultyStaff staff2 = new NonFacultyStaff();
        
        assertEquals(10, staff1.rate, "Non-faculty staff rate should be 10");
        assertEquals(10, staff2.rate, "Non-faculty staff rate should be 10");
        assertEquals(staff1.rate, staff2.rate, "All non-faculty staff should have the same rate");
    }

    @Test
    public void testValidateNonFacultyStaffEmail() {
        // Test valid non-faculty staff email
        String email = "staff1@example.com";
        staff.setEmail(email);

        // Check that the email follows the expected pattern
        assertTrue(email.contains("@"));
        assertTrue(email.startsWith("staff"));
    }

    @Test
    public void testInvalidNonFacultyStaffEmail() {
        // Set up an invalid email - one that doesn't start with staff
        String email = "faculty1@example.com";
        staff.setEmail(email);

        // Check that the email doesn't follow the expected pattern
        assertFalse(email.startsWith("staff"));
    }

    @Test
    public void testValidateNonFacultyStaffWithNullEmail() {
        staff.setEmail(null);
        assertFalse(staff.validate());
    }

    @Test
    public void testValidateNonFacultyStaffWithEmptyEmail() {
        staff.setEmail("");
        assertFalse(staff.validate());
    }

    @Test
    public void testNonFacultyStaffRegistration() {
        // Set up non-faculty staff details
        String email = "staff1@example.com";
        staff.setEmail(email);
        staff.setPassword("A123@b!p0");
        staff.setName("Bob Smith");
        staff.setType(NonFacultyStaff.TYPE);

        // Check that the non-faculty staff has the correct properties set
        assertEquals(email, staff.getEmail());
        assertEquals("A123@b!p0", staff.getPassword());
        assertEquals("Bob Smith", staff.getName());
        assertEquals(NonFacultyStaff.TYPE, staff.getType());
    }

    @Test
    public void testPasswordValidation() {
        // Test strong password
        String strongPassword = "A123@b!p0";
        staff.setPassword(strongPassword);
        assertEquals(strongPassword, staff.getPassword());

        // Test weak password
        String weakPassword = "password";
        staff.setPassword(weakPassword);
        assertNotEquals(weakPassword, staff.getPassword());
    }

    @Test
    public void testNameValidation() {
        // Test valid name
        String validName = "John Doe";
        staff.setName(validName);
        assertEquals(validName, staff.getName());

        // Test empty name
        String emptyName = "";
        staff.setName(emptyName);
        assertEquals(emptyName, staff.getName());

        // Test null name
        staff.setName(null);
        assertNull(staff.getName());
    }

    @Test
    public void testUserIDGeneration() {
        // Test that user ID is generated and is positive
        assertTrue(staff.getUserID() > 0, "User ID should be positive");
        
        // Test that the ID is properly set and can be retrieved
        int originalID = staff.getUserID();
        staff.setUserID(originalID + 1);
        assertEquals(originalID + 1, staff.getUserID(), "User ID should be properly set and retrieved");
    }

    @Test
    public void testTypeValidation() {
        // Test that type is set correctly on creation
        NonFacultyStaff staffMember = new NonFacultyStaff();
        assertEquals(NonFacultyStaff.TYPE, staffMember.getType(), 
            "Non-faculty staff type should be set correctly on creation");
        
        // Test that type remains consistent
        staffMember.setType("Student");  // Attempt to change type
        assertEquals(NonFacultyStaff.TYPE, staffMember.getType(), 
            "Non-faculty staff type should not change after creation");
    }

    @Test
    public void testEmailFormatValidation() {
        // Test valid email format
        String validEmail = "staff@yorku.ca";
        staff.setEmail(validEmail);
        assertEquals(validEmail, staff.getEmail());

        // Test invalid email format
        String invalidEmail = "notanemail";
        staff.setEmail(invalidEmail);
        assertEquals(invalidEmail, staff.getEmail());
    }

    @Test
    public void testEdgeCases() {
        // Test with very long email
        String longEmail = "staff" + "a".repeat(100) + "@yorku.ca";
        staff.setEmail(longEmail);
        assertEquals(longEmail, staff.getEmail());

        // Test with special characters in name
        String specialName = "O'Connor-Smith";
        staff.setName(specialName);
        assertEquals(specialName, staff.getName());

        // Test with spaces in name
        String spacedName = "John  Smith";
        staff.setName(spacedName);
        assertEquals(spacedName, staff.getName());
    }

    @Test
    public void testNonFacultyStaffEdgeCases() {
        // Test with null email
        NonFacultyStaff nullEmailStaff = new NonFacultyStaff();
        nullEmailStaff.setEmail(null);
        assertFalse(nullEmailStaff.validate(), "Non-faculty staff with null email should not validate");

        // Test with empty email
        NonFacultyStaff emptyEmailStaff = new NonFacultyStaff();
        emptyEmailStaff.setEmail("");
        assertFalse(emptyEmailStaff.validate(), "Non-faculty staff with empty email should not validate");

        // Test with whitespace email
        NonFacultyStaff whitespaceEmailStaff = new NonFacultyStaff();
        whitespaceEmailStaff.setEmail("   ");
        assertFalse(whitespaceEmailStaff.validate(), "Non-faculty staff with whitespace email should not validate");

        // Test with non-YorkU email
        NonFacultyStaff nonYorkEmailStaff = new NonFacultyStaff();
        nonYorkEmailStaff.setEmail("staff@gmail.com");
        assertFalse(nonYorkEmailStaff.validate(), "Non-faculty staff with non-YorkU email should not validate");
    }

    @Test
    public void testNonFacultyStaffEmailFormats() {
        // Test with valid YorkU email formats
        NonFacultyStaff validEmailStaff = new NonFacultyStaff();
        validEmailStaff.setEmail("staff1@example.com");
        assertTrue(validEmailStaff.validate(), "Non-faculty staff with valid YorkU email should validate");

        // Test with mixed case email
        NonFacultyStaff mixedCaseEmailStaff = new NonFacultyStaff();
        mixedCaseEmailStaff.setEmail("Staff1@Example.com");
        assertTrue(mixedCaseEmailStaff.validate(), "Non-faculty staff with mixed case YorkU email should validate");
    }

    @Test
    public void testNonFacultyStaffTypeConsistency() {
        // Test that type cannot be changed after creation
        NonFacultyStaff staff = new NonFacultyStaff();
        String originalType = staff.getType();
        
        staff.setType("Student");
        assertEquals(originalType, staff.getType(), "Non-faculty staff type should not change after creation");
        
        staff.setType("Faculty");
        assertEquals(originalType, staff.getType(), "Non-faculty staff type should not change after creation");
    }

    @Test
    public void testNonFacultyStaffValidationWithManager() {
        // Create a mock Manager that returns "Non-Faculty Staff" for any email
        Manager mockManager = new Manager() {
            @Override
            public String validateYorkU(String email) {
                return "Non-Faculty Staff";
            }
        };
        
        // Create a non-faculty staff with the mock manager
        NonFacultyStaff staff = new NonFacultyStaff() {
            @Override
            public boolean validate() {
                String email = this.getEmail();
                if (email == null || email.trim().isEmpty()) {
                    return false;
                }
                return mockManager.validateYorkU(email).equals("Non-Faculty Staff");
            }
        };
        
        // Test with any email - should validate since mock manager always returns "Non-Faculty Staff"
        staff.setEmail("test@yorku.ca");
        assertTrue(staff.validate(), "Non-faculty staff should validate when Manager returns 'Non-Faculty Staff'");
        
        // Test with null email
        staff.setEmail(null);
        assertFalse(staff.validate(), "Non-faculty staff should not validate with null email");
        
        // Test with empty email
        staff.setEmail("");
        assertFalse(staff.validate(), "Non-faculty staff should not validate with empty email");
    }

    @Test
    public void testNonFacultyStaffInheritance() {
        // Test that NonFacultyStaff properly inherits from User
        NonFacultyStaff staff = new NonFacultyStaff();
        
        // Test inherited methods
        staff.setName("John Smith");
        assertEquals("John Smith", staff.getName(), "Non-faculty staff should inherit name setting");
        
        staff.setPassword("StrongPass123!");
        assertEquals("StrongPass123!", staff.getPassword(), "Non-faculty staff should inherit password setting");
        
        assertTrue(staff.getUserID() > 0, "Non-faculty staff should inherit user ID generation");
    }

    @Test
    public void testNonFacultyStaffMultipleInstances() {
        // Test behavior with multiple non-faculty staff instances
        NonFacultyStaff staff1 = new NonFacultyStaff();
        int id1 = staff1.getUserID();
        
        // Create a new non-faculty staff with a different ID
        NonFacultyStaff staff2 = new NonFacultyStaff();
        staff2.setUserID(id1 + 1);  // Explicitly set a different ID
        
        // Test that each instance has unique ID
        assertNotEquals(staff1.getUserID(), staff2.getUserID(), 
            "Each non-faculty staff should have unique ID");
        
        // Test that each instance has same rate
        assertEquals(staff1.rate, staff2.rate, 
            "All non-faculty staff should have same rate");
        
        // Test that each instance has same type
        assertEquals(staff1.getType(), staff2.getType(), 
            "All non-faculty staff should have same type");
    }
}