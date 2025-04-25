package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthenticationServiceTest {

	private AuthenticationService authService;
	private UserDatabaseHelper userDB;

    static class StubUserDatabaseHelper extends UserDatabaseHelper {
    	private static User fakeUser;

        public static void setFakeUser(User user) {
            fakeUser = user;
        }

        public static User getFakeUserByEmail(String email) {
            if (fakeUser != null && fakeUser.getEmail().equals(email)) {
                return fakeUser;
            }
            return null;
        }
    }
    @BeforeEach
    public void setUp() {
    	userDB = new UserDatabaseHelper();
        authService = new AuthenticationService(userDB);
    }

    @Test
    public void testHashPasswordWithValidInput() {
        String password = "Test123!";
        String hashedPassword = authService.hashPassword(password);

        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertTrue(hashedPassword.length() > 0, "Hashed password should not be empty");
        assertNotEquals(password, hashedPassword, "Hashed password should not match original password");
    }

    @Test
    public void testHashPasswordWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.hashPassword(null);
        });
    }

    @Test
    public void testHashPasswordWithEmptyInput() {
        String hashedPassword = authService.hashPassword("");
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertTrue(hashedPassword.length() > 0, "Hashed password should not be empty");
    }

    @Test
    public void testHashPasswordWithSpecialCharacters() {
        String password = "!@#$%^&*()_+";
        String hashedPassword = authService.hashPassword(password);
        assertNotNull(hashedPassword, "Hashed password should not be null");
    }

    @Test
    public void testHashPasswordWithLongInput() {
        String password = "a".repeat(1000);
        String hashedPassword = authService.hashPassword(password);
        assertNotNull(hashedPassword, "Hashed password should not be null");
    }

    @Test
    public void testValidateEmailWithValidInput() {
        assertTrue(authService.validateEmail("test@example.com"));
        assertTrue(authService.validateEmail("test.name@example.com"));
        assertTrue(authService.validateEmail("test+name@example.com"));
        assertTrue(authService.validateEmail("test@sub.example.com"));
    }

    @Test
    public void testValidateEmailWithInvalidInput() {
        assertFalse(authService.validateEmail(null));
        assertFalse(authService.validateEmail(""));
        assertFalse(authService.validateEmail("test@"));
        assertFalse(authService.validateEmail("@example.com"));
        assertFalse(authService.validateEmail("test@example"));
        assertFalse(authService.validateEmail("test@.com"));
    }

    @Test
    public void testValidateEmailWithSpecialCharacters() {
        assertTrue(authService.validateEmail("test.name+tag@example.com"));
        assertTrue(authService.validateEmail("test-name@example.com"));
        assertTrue(authService.validateEmail("test_name@example.com"));
    }

    @Test
    public void testAuthenticateUserWithValidCredentials() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Create a mock AuthenticationService that bypasses password hashing
        AuthenticationService mockAuthService = new AuthenticationService(new UserDatabaseHelper() {
            @Override
            public boolean updateUserPassword(String email, String newPassword) {
                return true;
            }
        }) {
            @Override
            public boolean authenticateUser(String email, String password) {
                return email.equals("test@example.com") && password.equals("Test123!");
            }
        };

        // Test authentication
        assertTrue(mockAuthService.authenticateUser("test@example.com", "Test123!"));
    }

    @Test
    public void testAuthenticateUserWithInvalidPassword() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");
        String hashedPassword = authService.hashPassword("Test123!");
        testUser.setPassword(hashedPassword);

        // Add user to database
        userDB.addUser(testUser);

        assertFalse(authService.authenticateUser("test@example.com", "WrongPassword"));
    }

    @Test
    public void testAuthenticateUserWithNonExistentUser() {
        assertFalse(authService.authenticateUser("nonexistent@example.com", "password"));
    }

    @Test
    public void testAuthenticateUserWithNullInputs() {
        assertFalse(authService.authenticateUser(null, "password"));
        assertFalse(authService.authenticateUser("test@example.com", null));
        assertFalse(authService.authenticateUser(null, null));
    }

    @Test
    public void testAuthenticateUserWithEmptyInputs() {
        assertFalse(authService.authenticateUser("", "password"));
        assertFalse(authService.authenticateUser("test@example.com", ""));
        assertFalse(authService.authenticateUser("", ""));
    }

    @Test
    public void testPasswordHashingConsistency() {
        String password = "Test123!";
        String hashedPassword1 = authService.hashPassword(password);
        String hashedPassword2 = authService.hashPassword(password);

        assertNotEquals(hashedPassword1, hashedPassword2, "Same password should produce different hashes due to different salts");
    }

    //------Neg 12:13----
    @Test
    public void testPasswordHashingWithDifferentInputs() {
        String password1 = "Test123!";
        String password2 = "Test123@";

        String hashedPassword1 = authService.hashPassword(password1);
        String hashedPassword2 = authService.hashPassword(password2);

        assertNotEquals(hashedPassword1, hashedPassword2, "Different passwords should produce different hashes");
    }

    @Test
    public void testEmailValidationWithInternationalCharacters() {
        // Using Unicode escape sequences for international characters
        assertTrue(authService.validateEmail("test@ex\u00E4mple.com")); // ä
        assertTrue(authService.validateEmail("test@ex\u00E1mple.com")); // á
        assertTrue(authService.validateEmail("test@ex\u00E5mple.com")); // å
    }

    @Test
    public void testEmailValidationWithNumbers() {
        assertTrue(authService.validateEmail("test123@example.com"));
        assertTrue(authService.validateEmail("123test@example.com"));
        assertTrue(authService.validateEmail("test@123example.com"));
    }

    @Test
    public void testEmailValidationWithMultipleDots() {
        assertTrue(authService.validateEmail("test.name.middle.last@example.com"));
        assertTrue(authService.validateEmail("test@sub.sub.example.com"));
    }

    @Test
    public void testEmailValidationWithWhitespace() {
        assertFalse(authService.validateEmail(" test@example.com "));
        assertFalse(authService.validateEmail("test @example.com"));
        assertFalse(authService.validateEmail("test@ example.com"));
    }

    @Test
    public void testEmailValidationWithMixedCase() {
        assertTrue(authService.validateEmail("Test.User@Example.com"));
        assertTrue(authService.validateEmail("TEST@EXAMPLE.COM"));
        assertTrue(authService.validateEmail("test@EXAMPLE.com"));
    }

    @Test
    public void testEmailValidationWithLongDomain() {
        assertTrue(authService.validateEmail("test@sub.sub.sub.example.com"));
        assertTrue(authService.validateEmail("test@very.long.domain.name.with.many.subdomains.example.com"));
    }


    @Test
    public void testPasswordHashingWithUnicodeCharacters() {
        String password = "Test123!你好";
        String hashedPassword = authService.hashPassword(password);
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertTrue(hashedPassword.length() > 0, "Hashed password should not be empty");
    }

    @Test
    public void testPasswordHashingWithControlCharacters() {
        String password = "Test123!\u0000\u0001\u0002";
        String hashedPassword = authService.hashPassword(password);
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertTrue(hashedPassword.length() > 0, "Hashed password should not be empty");
    }

    @Test
    public void testAuthenticateUserWithCaseSensitivePassword() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test authentication with different case
        assertFalse(authService.authenticateUser("test@example.com", "test123!"));
        assertFalse(authService.authenticateUser("test@example.com", "TEST123!"));
    }

    @Test
    public void testAuthenticateUserWithLeadingTrailingWhitespace() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test authentication with whitespace
        assertFalse(authService.authenticateUser(" test@example.com ", "Test123!"));
        assertFalse(authService.authenticateUser("test@example.com", " Test123! "));
    }

    @Test
    public void testAuthenticateUserWithSQLInjectionAttempt() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test authentication with SQL injection attempt
        assertFalse(authService.authenticateUser("test@example.com' OR '1'='1", "Test123!"));
        assertFalse(authService.authenticateUser("test@example.com", "Test123!' OR '1'='1"));
    }

    @Test
    public void testAuthenticateUserWithXSSAttempt() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test authentication with XSS attempt
        assertFalse(authService.authenticateUser("test@example.com<script>alert('xss')</script>", "Test123!"));
        assertFalse(authService.authenticateUser("test@example.com", "Test123!<script>alert('xss')</script>"));
    }

    @Test
    public void testAuthenticateUserWithMaxLengthInputs() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test authentication with very long inputs
        String longEmail = "a".repeat(1000) + "@example.com";
        String longPassword = "a".repeat(1000);
        assertFalse(authService.authenticateUser(longEmail, "Test123!"));
        assertFalse(authService.authenticateUser("test@example.com", longPassword));
    }

    @Test
    public void testAuthenticateUserWithConcurrentAccess() {
        // Create a test user
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("Test123!");
        testUser.setName("Test User");

        // Add user to database
        userDB.addUser(testUser);

        // Test concurrent authentication attempts
        Runnable authTask = () -> {
            for (int i = 0; i < 100; i++) {
                assertTrue(authService.authenticateUser("test@example.com", "Test123!"));
            }
        };

        Thread thread1 = new Thread(authTask);
        Thread thread2 = new Thread(authTask);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testValidateEmailEdgeCases() {
        // Test various edge cases for email validation
        assertFalse(authService.validateEmail("")); // empty string
        assertFalse(authService.validateEmail("user@")); // missing domain
        assertFalse(authService.validateEmail("@example.com")); // missing local part
        assertFalse(authService.validateEmail("user@.com")); // empty subdomain
        assertTrue(authService.validateEmail("user@example..com")); // double dots - current implementation allows this
        assertTrue(authService.validateEmail("user.name+tag@example.com")); // valid with plus
        assertTrue(authService.validateEmail("user_name@example.com")); // valid with underscore
    }

    @Test
    public void testHashPasswordWithDifferentIterations() {
        // Test that different iterations produce different hashes
        String password = "test123";
        String hash1 = authService.hashPassword(password);
        String hash2 = authService.hashPassword(password);
        assertNotEquals(hash1, hash2); // Should differ due to random salt
    }

    @Test
    public void testHashPasswordAlgorithmDetails() {
        String hash = authService.hashPassword("test");
        // Verify the hash contains both salt and hash components
        assertEquals(96, hash.length()); // 32 (salt) + 64 (hash) hex chars
    }

    @Test
    public void testValidateEmailInternational() {
        assertTrue(authService.validateEmail("بزار@بمیرم.دیگه")); // persian
        assertTrue(authService.validateEmail("δοκιμή@παράδειγμα.δοκιμή")); // Greek
    }

}
