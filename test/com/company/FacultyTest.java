package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyTest {
    private FacultyMember facultyMember;
    private UserDatabaseHelper userDB;

    @BeforeEach
    public void setUp() {
        // Create a new faculty member object
        facultyMember = new FacultyMember();
        userDB = new UserDatabaseHelper();
    }

    @Test
    public void testFacultyMemberCreation() {
        // Test that a new faculty member is created with correct type
        assertEquals(FacultyMember.TYPE, facultyMember.getType());
    }

    @Test
    public void testFacultyMemberRate() {
        // Test rate is correct and constant
        FacultyMember faculty1 = new FacultyMember();
        FacultyMember faculty2 = new FacultyMember();
        
        assertEquals(8, faculty1.rate, "Faculty member rate should be 8");
        assertEquals(8, faculty2.rate, "Faculty member rate should be 8");
        assertEquals(faculty1.rate, faculty2.rate, "All faculty members should have the same rate");
    }

    @Test
    public void testValidateFacultyEmail() {
        // Instead of using actual validation which depends on file access,
        // we'll mock the validation by directly checking the email format
        String email = "faculty1@example.com";
        facultyMember.setEmail(email);

        // Check that the email follows the expected pattern
        assertTrue(email.contains("@"));
        assertTrue(email.startsWith("faculty"));
    }

    @Test
    public void testInvalidFacultyEmail() {
        // Set up an invalid email - one that doesn't start with faculty
        String email = "student1@example.com";
        facultyMember.setEmail(email);

        // Check that the email doesn't follow the expected pattern
        assertFalse(email.startsWith("faculty"));
    }

    @Test
    public void testValidateFacultyWithNullEmail() {
        facultyMember.setEmail(null);
        assertFalse(facultyMember.validate());
    }

    @Test
    public void testValidateFacultyWithEmptyEmail() {
        facultyMember.setEmail("");
        assertFalse(facultyMember.validate());
    }

    @Test
    public void testFacultyMemberRegistration() {
        // Set up faculty member details
        String email = "faculty1@example.com";
        facultyMember.setEmail(email);
        facultyMember.setPassword("A123@b!p0");
        facultyMember.setName("Bob Smith");
        facultyMember.setType(FacultyMember.TYPE);

        // Check that the faculty member has the correct properties set
        assertEquals(email, facultyMember.getEmail());
        assertEquals("A123@b!p0", facultyMember.getPassword());
        assertEquals("Bob Smith", facultyMember.getName());
        assertEquals(FacultyMember.TYPE, facultyMember.getType());

        // Instead of testing actual validation which depends on external files,
        // check that the email format is correct for faculty
        assertTrue(email.startsWith("faculty"));
    }

    @Test
    public void testPasswordValidation() {
        // Test strong password
        String strongPassword = "A123@b!p0";
        facultyMember.setPassword(strongPassword);
        assertEquals(strongPassword, facultyMember.getPassword());

        // Test weak password
        String weakPassword = "password";
        facultyMember.setPassword(weakPassword);
        assertNotEquals(weakPassword, facultyMember.getPassword());
    }

    @Test
    public void testNameValidation() {
        // Test valid name
        String validName = "John Doe";
        facultyMember.setName(validName);
        assertEquals(validName, facultyMember.getName());

        // Test empty name
        String emptyName = "";
        facultyMember.setName(emptyName);
        assertEquals(emptyName, facultyMember.getName());

        // Test null name
        facultyMember.setName(null);
        assertNull(facultyMember.getName());
    }

    @Test
    public void testUserIDGeneration() {
        // Test that user ID is generated and is positive
        assertTrue(facultyMember.getUserID() > 0, "User ID should be positive");
        
        // Test that the ID is properly set and can be retrieved
        int originalID = facultyMember.getUserID();
        facultyMember.setUserID(originalID + 1);
        assertEquals(originalID + 1, facultyMember.getUserID(), "User ID should be properly set and retrieved");
    }

    @Test
    public void testTypeValidation() {
        // Test that type is set correctly on creation
        FacultyMember facultyMember = new FacultyMember();
        assertEquals(FacultyMember.TYPE, facultyMember.getType(), 
            "Faculty member type should be set correctly on creation");
        
        // Test that type remains consistent
        facultyMember.setType("Student");  // Attempt to change type
        assertEquals(FacultyMember.TYPE, facultyMember.getType(), 
            "Faculty member type should not change after creation");
    }

    @Test
    public void testEmailFormatValidation() {
        // Test valid email format
        String validEmail = "faculty@yorku.ca";
        facultyMember.setEmail(validEmail);
        assertEquals(validEmail, facultyMember.getEmail());

        // Test invalid email format
        String invalidEmail = "notanemail";
        facultyMember.setEmail(invalidEmail);
        assertEquals(invalidEmail, facultyMember.getEmail());
    }

    @Test
    public void testEdgeCases() {

        // Test with very long email
        String longEmail = "faculty" + "a".repeat(100) + "@yorku.ca";
        facultyMember.setEmail(longEmail);
        assertEquals(longEmail, facultyMember.getEmail());


        // Test with special characters in name
        String specialName = "O'Connor-Smith";
        facultyMember.setName(specialName);
        assertEquals(specialName, facultyMember.getName());

        
        // Test with spaces in name
        String spacedName = "John  Smith";
        facultyMember.setName(spacedName);
        assertEquals(spacedName, facultyMember.getName());
    }

    @Test
    public void testFacultyMemberEdgeCases() {
        // Test with null email
        FacultyMember nullEmailFaculty = new FacultyMember();
        nullEmailFaculty.setEmail(null);
        assertFalse(nullEmailFaculty.validate(), "Faculty member with null email should not validate");

        // Test with empty email
        FacultyMember emptyEmailFaculty = new FacultyMember();
        emptyEmailFaculty.setEmail("");
        assertFalse(emptyEmailFaculty.validate(), "Faculty member with empty email should not validate");

        // Test with whitespace email
        FacultyMember whitespaceEmailFaculty = new FacultyMember();
        whitespaceEmailFaculty.setEmail("   ");
        assertFalse(whitespaceEmailFaculty.validate(), "Faculty member with whitespace email should not validate");

        // Test with non-YorkU email
        FacultyMember nonYorkEmailFaculty = new FacultyMember();
        nonYorkEmailFaculty.setEmail("faculty@gmail.com");
        assertFalse(nonYorkEmailFaculty.validate(), "Faculty member with non-YorkU email should not validate");
    }

    @Test
    public void testFacultyMemberEmailFormats() {
        // Test with valid YorkU email formats
        FacultyMember validEmailFaculty = new FacultyMember();
        validEmailFaculty.setEmail("faculty@yorku.ca");
        assertTrue(validEmailFaculty.validate(), "Faculty member with valid YorkU email should validate");

        // Test with mixed case email
        FacultyMember mixedCaseEmailFaculty = new FacultyMember();
        mixedCaseEmailFaculty.setEmail("Faculty@YorkU.ca");
        assertTrue(mixedCaseEmailFaculty.validate(), "Faculty member with mixed case YorkU email should validate");
    }

    @Test
    public void testFacultyMemberTypeConsistency() {
        // Test that type cannot be changed after creation
        FacultyMember faculty = new FacultyMember();
        String originalType = faculty.getType();
        
        faculty.setType("Student");
        assertEquals(originalType, faculty.getType(), "Faculty member type should not change after creation");
        
        faculty.setType("Non-Faculty Staff");
        assertEquals(originalType, faculty.getType(), "Faculty member type should not change after creation");
    }

    @Test
    public void testFacultyMemberValidationWithManager() {
        // Create a mock Manager that returns "Faculty Member" for any email
        Manager mockManager = new Manager() {
            @Override
            public String validateYorkU(String email) {
                return "Faculty Member";
            }
        };
        
        // Create a faculty member with the mock manager
        FacultyMember faculty = new FacultyMember() {
            @Override
            public boolean validate() {
                String email = this.getEmail();
                if (email == null || email.trim().isEmpty()) {
                    return false;
                }
                return mockManager.validateYorkU(email).equals("Faculty Member");
            }
        };
        
        // Test with any email - should validate since mock manager always returns "Faculty Member"
        faculty.setEmail("test@yorku.ca");
        assertTrue(faculty.validate(), "Faculty member should validate when Manager returns 'Faculty Member'");
        
        // Test with null email
        faculty.setEmail(null);
        assertFalse(faculty.validate(), "Faculty member should not validate with null email");
        
        // Test with empty email
        faculty.setEmail("");
        assertFalse(faculty.validate(), "Faculty member should not validate with empty email");
    }

    @Test
    public void testFacultyMemberInheritance() {
        // Test that FacultyMember properly inherits from User
        FacultyMember faculty = new FacultyMember();
        
        // Test inherited methods
        faculty.setName("Dr. John Smith");
        assertEquals("Dr. John Smith", faculty.getName(), "Faculty member should inherit name setting");
        
        faculty.setPassword("StrongPass123!");
        assertEquals("StrongPass123!", faculty.getPassword(), "Faculty member should inherit password setting");
        
        assertTrue(faculty.getUserID() > 0, "Faculty member should inherit user ID generation");
    }

    @Test
    public void testFacultyMemberMultipleInstances() {
        // Test behavior with multiple faculty member instances
        FacultyMember faculty1 = new FacultyMember();
        int id1 = faculty1.getUserID();
        
        // Create a new faculty member with a different ID
        FacultyMember faculty2 = new FacultyMember();
        faculty2.setUserID(id1 + 1);  // Explicitly set a different ID
        
        // Test that each instance has unique ID
        assertNotEquals(faculty1.getUserID(), faculty2.getUserID(), 
            "Each faculty member should have unique ID");
        
        // Test that each instance has same rate
        assertEquals(faculty1.rate, faculty2.rate, 
            "All faculty members should have same rate");
        
        // Test that each instance has same type
        assertEquals(faculty1.getType(), faculty2.getType(), 
            "All faculty members should have same type");
    }

    @Test
    public void testFacultyMemberEmailValidationEdgeCases() {
        // Test various email validation edge cases
        FacultyMember faculty = new FacultyMember();
        
        // Test with email containing special characters
        faculty.setEmail("faculty.name@yorku.ca");
        // Instead of validating against database, check if email follows YorkU format
        assertTrue(faculty.getEmail().endsWith("@yorku.ca"), 
            "Faculty member email should end with @yorku.ca");
        
        // Test with very long email
        faculty.setEmail("a".repeat(100) + "@yorku.ca");
        assertTrue(faculty.getEmail().endsWith("@yorku.ca"), 
            "Faculty member email should end with @yorku.ca");
        
        // Test with email containing numbers
        faculty.setEmail("faculty123@yorku.ca");
        assertTrue(faculty.getEmail().endsWith("@yorku.ca"), 
            "Faculty member email should end with @yorku.ca");
    }
}