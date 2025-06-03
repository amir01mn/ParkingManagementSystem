package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class UserTest {

	private User user;

	@BeforeEach
	public void setUp() {

		user = new User();
	}

	@Test
	public void testConstructorIncrementUserID() {

		int lastID = UserDatabaseHelper.getLastUserID();
		User u = new User();

		assertEquals(lastID + 1, u.getUserID());
	}

	@Test
	public void testConstructorUserID() {

		User u = new User(10);
		assertEquals(10, u.getUserID());
	}

	@Test
	public void testConstructorIDEmailPass() {

		User u = new User(12, "melissa@gmail.com", "Hkdhay3!kakuH");

		assertEquals(12, u.getUserID());
		assertEquals("melissa@gmail.com", u.getEmail());
		assertEquals("Hkdhay3!kakuH", u.getPassword());
	}

	@Test
    public void testGetID() {
        User u = new User(15, "melissa@gmail.com", "Hkdhay3!kakuH");
        assertEquals(15, u.getUserID());
    }

	@Test
    public void testSetAndGetName() {
        user.setName("Mel Melis");
        assertEquals("Mel Melis", user.getName());
    }

	@Test
    public void testSetAndGetEmail() {
        user.setEmail("melissa@gmail.com");
        assertEquals("melissa@gmail.com", user.getEmail());
    }

	@Test
	public void testSetAndGetType() {
		user.setType("Student");
		assertEquals("Student", user.getType());
	}

	@Test
	public void testSetAndGetType2() {
		user.setEmail("melissa@gmail.com");
		user.setPassword("Hkdhay3!kakuH");
		user.setType("Student");
		assertEquals("Student", user.getType());
	}

	@Test
	public void testSetAndGetPasswordStrong() {
		user.setPassword("Hkdhay3!kakuH");;
		assertEquals("Hkdhay3!kakuH", user.getPassword());
	}

	@Test
	public void testSetAndGetPasswordWeak() {
		user.setPassword("password");;
		assertNull(user.getPassword());
	}

	@Test
	public void testDefaultUserCreation() {
		// Test that a new user is created with default values
		assertNotNull(user);
		assertNull(user.getName());
		assertNull(user.getEmail());
		assertNull(user.getPassword());
		assertNull(user.getType());
		assertTrue(user.getUserID() > 0);
	}

	@Test
	public void testUserSettersAndGetters() {
		// Test setting and getting all user properties
		String name = "John Doe";
		String email = "john@example.com";
		String password = "StrongPass123!";
		String type = "Student";
		int userId = 1001;

		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setType(type);
		user.setUserID(userId);

		assertEquals(name, user.getName());
		assertEquals(email, user.getEmail());
		assertEquals(password, user.getPassword());
		assertEquals(type, user.getType());
		assertEquals(userId, user.getUserID());
	}

	@Test
	public void testPasswordStrengthValidation() {
		// Test various password strength scenarios
		String strongPassword = "StrongPass123!";
		String weakPassword = "weak";
		String mediumPassword = "Medium123";

		user.setPassword(strongPassword);
		assertEquals(strongPassword, user.getPassword(), "Strong password should be accepted");

		user.setPassword(weakPassword);
		assertNotEquals(weakPassword, user.getPassword(), "Weak password should be rejected");

		user.setPassword(mediumPassword);
		assertNotEquals(mediumPassword, user.getPassword(), "Medium strength password should be rejected");
	}

	@Test
	public void testEmailValidation() {
		// Test various email validation scenarios
		String validEmail = "test@example.com";
		String invalidEmail = "notanemail";
		String emptyEmail = "";
		String nullEmail = null;

		user.setEmail(validEmail);
		assertEquals(validEmail, user.getEmail(), "Valid email should be accepted");

		user.setEmail(invalidEmail);
		assertEquals(invalidEmail, user.getEmail(), "Invalid email should still be set");

		user.setEmail(emptyEmail);
		assertEquals(emptyEmail, user.getEmail(), "Empty email should be accepted");

		user.setEmail(nullEmail);
		assertNull(user.getEmail(), "Null email should be accepted");
	}

	@Test
	public void testNameValidation() {
		// Test various name validation scenarios
		String validName = "John Doe";
		String emptyName = "";
		String nullName = null;
		String longName = "a".repeat(100);

		user.setName(validName);
		assertEquals(validName, user.getName(), "Valid name should be accepted");

		user.setName(emptyName);
		assertEquals(emptyName, user.getName(), "Empty name should be accepted");

		user.setName(nullName);
		assertNull(user.getName(), "Null name should be accepted");

		user.setName(longName);
		assertEquals(longName, user.getName(), "Long name should be accepted");
	}

	@Test
	public void testUserTypeValidation() {
		// Test various user type scenarios
		String validType = "Student";
		String emptyType = "";
		String nullType = null;
		String invalidType = "InvalidType";

		user.setType(validType);
		assertEquals(validType, user.getType(), "Valid type should be accepted");

		user.setType(emptyType);
		assertEquals(emptyType, user.getType(), "Empty type should be accepted");

		user.setType(nullType);
		assertNull(user.getType(), "Null type should be accepted");

		user.setType(invalidType);
		assertEquals(invalidType, user.getType(), "Invalid type should still be set");
	}

	@Test
	public void testUserIDValidation() {
		// Test various user ID scenarios
		int positiveID = 1001;
		int zeroID = 0;
		int negativeID = -1;

		user.setUserID(positiveID);
		assertEquals(positiveID, user.getUserID(), "Positive ID should be accepted");

		user.setUserID(zeroID);
		assertEquals(zeroID, user.getUserID(), "Zero ID should be accepted");

		user.setUserID(negativeID);
		assertEquals(negativeID, user.getUserID(), "Negative ID should be accepted");
	}

	@Test
	public void testUserValidation() {
		// Test the validate method with various scenarios
		User validUser = new User();
		validUser.setEmail("test@example.com");
		assertTrue(validUser.validate(), "User with valid email should validate");

		User invalidUser = new User();
		invalidUser.setEmail("");
		assertFalse(invalidUser.validate(), "User with empty email should not validate");

		User nullEmailUser = new User();
		nullEmailUser.setEmail(null);
		assertFalse(nullEmailUser.validate(), "User with null email should not validate");
	}

	@Test
	public void testPasswordEdgeCases() {
		// Test password with minimum length (8 characters)
		String minLengthPassword = "A1!bcdef";
		user.setPassword(minLengthPassword);
		assertEquals(minLengthPassword, user.getPassword(), "Minimum length password should be accepted");

		// Test password with maximum length (assuming 100 characters)
		String maxLengthPassword = "A1!" + "a".repeat(97);
		user.setPassword(maxLengthPassword);
		assertEquals(maxLengthPassword, user.getPassword(), "Maximum length password should be accepted");

		// Test password with all special characters
		String allSpecialPassword = "!@#$%^&*()_+";
		user.setPassword(allSpecialPassword);
		assertNotEquals(allSpecialPassword, user.getPassword(), "Password with only special characters should be rejected");

		// Test password with only numbers
		String onlyNumbersPassword = "12345678";
		user.setPassword(onlyNumbersPassword);
		assertNotEquals(onlyNumbersPassword, user.getPassword(), "Password with only numbers should be rejected");

		// Test password with only letters
		String onlyLettersPassword = "abcdefgh";
		user.setPassword(onlyLettersPassword);
		assertNotEquals(onlyLettersPassword, user.getPassword(), "Password with only letters should be rejected");
	}

	@Test
	public void testEmailEdgeCases() {
		// Test email with maximum length
		String longEmail = "a".repeat(64) + "@" + "b".repeat(63) + ".com";
		user.setEmail(longEmail);
		assertEquals(longEmail, user.getEmail(), "Long email should be accepted");

		// Test email with special characters
		String specialCharEmail = "user.name+tag@example.com";
		user.setEmail(specialCharEmail);
		assertEquals(specialCharEmail, user.getEmail(), "Email with special characters should be accepted");

		// Test email with multiple dots
		String multiDotEmail = "user.name.middle.last@example.com";
		user.setEmail(multiDotEmail);
		assertEquals(multiDotEmail, user.getEmail(), "Email with multiple dots should be accepted");

		// Test email with international characters
		String internationalEmail = "user@exämple.com";
		user.setEmail(internationalEmail);
		assertEquals(internationalEmail, user.getEmail(), "Email with international characters should be accepted");
	}

	@Test
	public void testNameEdgeCases() {
		// Test name with special characters
		String specialCharName = "O'Connor-Smith";
		user.setName(specialCharName);
		assertEquals(specialCharName, user.getName(), "Name with special characters should be accepted");

		// Test name with international characters
		String internationalName = "José García";
		user.setName(internationalName);
		assertEquals(internationalName, user.getName(), "Name with international characters should be accepted");

		// Test name with numbers
		String nameWithNumbers = "John Smith 123";
		user.setName(nameWithNumbers);
		assertEquals(nameWithNumbers, user.getName(), "Name with numbers should be accepted");

		// Test name with multiple spaces
		String nameWithSpaces = "John  Smith";
		user.setName(nameWithSpaces);
		assertEquals(nameWithSpaces, user.getName(), "Name with multiple spaces should be accepted");
	}

	@Test
	public void testUserTypeEdgeCases() {
		// Test type with special characters
		String specialCharType = "Admin-User";
		user.setType(specialCharType);
		assertEquals(specialCharType, user.getType(), "Type with special characters should be accepted");

		// Test type with numbers
		String typeWithNumbers = "User123";
		user.setType(typeWithNumbers);
		assertEquals(typeWithNumbers, user.getType(), "Type with numbers should be accepted");

		// Test type with spaces
		String typeWithSpaces = "System Admin";
		user.setType(typeWithSpaces);
		assertEquals(typeWithSpaces, user.getType(), "Type with spaces should be accepted");

		// Test type with international characters
		String internationalType = "Administratör";
		user.setType(internationalType);
		assertEquals(internationalType, user.getType(), "Type with international characters should be accepted");
	}

	@Test
	public void testUserIDEdgeCases() {
		// Test maximum integer value
		user.setUserID(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, user.getUserID(), "Maximum integer ID should be accepted");

		// Test minimum integer value
		user.setUserID(Integer.MIN_VALUE);
		assertEquals(Integer.MIN_VALUE, user.getUserID(), "Minimum integer ID should be accepted");

		// Test ID with large value
		user.setUserID(999999999);
		assertEquals(999999999, user.getUserID(), "Large ID should be accepted");

		// Test ID with small value
		user.setUserID(-999999999);
		assertEquals(-999999999, user.getUserID(), "Small ID should be accepted");
	}


	@Test
	public void testUserValidationEdgeCases() {
		// Test validation with whitespace email
		User whitespaceEmailUser = new User();
		whitespaceEmailUser.setEmail("   test@example.com   ");
		assertTrue(whitespaceEmailUser.validate(), "User with whitespace email should validate");

		// Test validation with mixed case email
		User mixedCaseEmailUser = new User();
		mixedCaseEmailUser.setEmail("Test@Example.com");
		assertTrue(mixedCaseEmailUser.validate(), "User with mixed case email should validate");

		// Test validation with subdomain email
		User subdomainEmailUser = new User();
		subdomainEmailUser.setEmail("test@sub.example.com");
		assertTrue(subdomainEmailUser.validate(), "User with subdomain email should validate");

		// Test validation with numeric email
		User numericEmailUser = new User();
		numericEmailUser.setEmail("123@456.com");
		assertTrue(numericEmailUser.validate(), "User with numeric email should validate");
	}

	@Test
        public void testUserPropertyCombinations() {
		// Test setting all properties with edge case values
		String edgeCaseName = "John O'Connor-Smith 123";
		String edgeCaseEmail = "john.123@sub.example.com";
		String edgeCasePassword = "A1!bcdefgh";
		String edgeCaseType = "Admin-User 123";
		int edgeCaseID = Integer.MAX_VALUE;

		user.setName(edgeCaseName);
		user.setEmail(edgeCaseEmail);
		user.setPassword(edgeCasePassword);
		user.setType(edgeCaseType);
		user.setUserID(edgeCaseID);

		assertEquals(edgeCaseName, user.getName(), "Edge case name should be preserved");
		assertEquals(edgeCaseEmail, user.getEmail(), "Edge case email should be preserved");
		assertEquals(edgeCasePassword, user.getPassword(), "Edge case password should be preserved");
                assertEquals(edgeCaseType, user.getType(), "Edge case type should be preserved");
                assertEquals(edgeCaseID, user.getUserID(), "Edge case ID should be preserved");
        }

        @Test
        public void testSetHashedPassword() {
                AuthenticationService auth = new AuthenticationService(new UserDatabaseHelper());
                String hashed = auth.hashPassword("Test123!");
                user.setPassword(hashed);
                assertEquals(hashed, user.getPassword());
        }

}
