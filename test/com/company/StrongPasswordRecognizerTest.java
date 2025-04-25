package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StrongPasswordRecognizerTest {

	@Test
    public void testNullPassword() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong(null));
    }

    @Test
    public void testShortPassword() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("Ab1@"));
    }

    @Test
    public void testNoUppercase() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("onlylower1!"));
    }

    @Test
    public void testNoLowercase() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("ONLYUPPER1!"));
    }

    @Test
    public void testNoDigit() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("HasNoDig!"));
    }

    @Test
    public void testNoSpecialCharacter() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("HasNoSpecial1"));
    }

    @Test
    public void testValidStrongPassword() {
        assertTrue(StrongPasswordRecognizer.isPasswordStrong("AhudiHg2!"));
    }

    @Test
    public void testValidLongStrongPassword() {
        assertTrue(StrongPasswordRecognizer.isPasswordStrong("A1@bcdefghijk"));
    }

    @Test
    public void testExtraCase1() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("Abcdefgh@"));
    }

    @Test
    public void testExtraCase2() {
        assertFalse(StrongPasswordRecognizer.isPasswordStrong("Abcdefg1"));
    }
}
