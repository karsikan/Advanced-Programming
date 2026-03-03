package com.oceanview.service;

import com.oceanview.util.DateUtil;
import com.oceanview.util.Validator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validator & DateUtil Tests")
class ValidatorTest {

    @Test
    @DisplayName("Non-empty string is valid")
    void testIsNotEmpty() {
        assertTrue(Validator.isNotEmpty("hello"));
        assertFalse(Validator.isNotEmpty(""));
        assertFalse(Validator.isNotEmpty("   "));
        assertFalse(Validator.isNotEmpty(null));
    }

    @Test
    @DisplayName("Valid 10-digit phone numbers")
    void testValidPhone() {
        assertTrue(Validator.isValidPhone("0771234567"));
        assertTrue(Validator.isValidPhone("0112345678"));
    }

    @Test
    @DisplayName("Invalid phone numbers")
    void testInvalidPhone() {
        assertFalse(Validator.isValidPhone("077123")); // too short
        assertFalse(Validator.isValidPhone("07712345678")); // too long
        assertFalse(Validator.isValidPhone("077abc1234")); // letters
        assertFalse(Validator.isValidPhone(null));
    }

    @Test
    @DisplayName("Valid date format YYYY-MM-DD")
    void testValidDate() {
        assertTrue(Validator.isValidDate("2024-05-20"));
        assertTrue(Validator.isValidDate("2024-12-31"));
    }

    @Test
    @DisplayName("Invalid date formats")
    void testInvalidDate() {
        assertFalse(Validator.isValidDate("20-05-2024")); // wrong format
        assertFalse(Validator.isValidDate("2024/05/20")); // wrong separator
        assertFalse(Validator.isValidDate("not-a-date"));
        assertFalse(Validator.isValidDate(null));
    }

    @Test
    @DisplayName("Calculate nights between dates")
    void testCalculateNights() {
        assertEquals(3, DateUtil.calculateNights("2024-05-01", "2024-05-04"));
        assertEquals(1, DateUtil.calculateNights("2024-12-31", "2025-01-01"));
        assertEquals(0, DateUtil.calculateNights("2024-05-01", "2024-05-01"));
    }

    @Test
    @DisplayName("Checkout date must be after check-in")
    void testCheckOutAfterCheckIn() {
        assertTrue(DateUtil.isCheckOutAfterCheckIn("2024-05-01", "2024-05-05"));
        assertFalse(DateUtil.isCheckOutAfterCheckIn("2024-05-05", "2024-05-01")); // reversed
        assertFalse(DateUtil.isCheckOutAfterCheckIn("2024-05-01", "2024-05-01")); // same day
    }

    @Test
    @DisplayName("Valid reservation ID prefix")
    void testValidReservationId() {
        assertTrue(Validator.isValidReservationId("OVR-20240220-1234"));
        assertFalse(Validator.isValidReservationId("ABC-12345"));
        assertFalse(Validator.isValidReservationId(null));
    }
}
