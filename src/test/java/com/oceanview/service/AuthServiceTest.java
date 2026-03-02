package com.oceanview.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    @Order(1)
    @DisplayName("Valid admin login should succeed")
    void testValidAdminLogin() {
        boolean result = authService.login("admin", "admin123");
        assertTrue(result, "Admin login should succeed with correct credentials");
    }

    @Test
    @Order(2)
    @DisplayName("Valid staff login should succeed")
    void testValidStaffLogin() {
        boolean result = authService.login("staff", "staff123");
        assertTrue(result, "Staff login should succeed with correct credentials");
    }

    @Test
    @Order(3)
    @DisplayName("Invalid password should fail login")
    void testInvalidPassword() {
        authService.login("admin", "admin123"); // ensure user exists
        AuthService newAuth = new AuthService();
        boolean result = newAuth.login("admin", "wrongpassword");
        assertFalse(result, "Login should fail with incorrect password");
    }

    @Test
    @Order(4)
    @DisplayName("Non-existent user should fail login")
    void testNonExistentUser() {
        boolean result = authService.login("unknown_user", "password");
        assertFalse(result, "Login should fail for non-existent user");
    }

    @Test
    @Order(5)
    @DisplayName("Empty username should fail login")
    void testEmptyUsername() {
        boolean result = authService.login("", "password");
        assertFalse(result, "Login should fail with empty username");
    }

    @Test
    @Order(6)
    @DisplayName("Empty password should fail login")
    void testEmptyPassword() {
        boolean result = authService.login("admin", "");
        assertFalse(result, "Login should fail with empty password");
    }

    @Test
    @Order(7)
    @DisplayName("Null credentials should fail login")
    void testNullCredentials() {
        boolean result = authService.login(null, null);
        assertFalse(result, "Login should fail with null credentials");
    }

    @Test
    @Order(8)
    @DisplayName("Admin role should be set after login")
    void testAdminRoleAfterLogin() {
        authService.login("admin", "admin123");
        assertEquals("ADMIN", AuthService.getLoggedInRole(), "Role should be ADMIN after admin login");
    }

    @Test
    @Order(9)
    @DisplayName("isAdmin() should return true for admin")
    void testIsAdmin() {
        authService.login("admin", "admin123");
        assertTrue(AuthService.isAdmin(), "isAdmin should return true for admin user");
    }
}
