package com.oceanview.service;

import com.oceanview.model.User;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
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
    @DisplayName("Valid admin login should return Admin user")
    void testValidAdminLogin() {
        // The database contains a pre-hashed password for admin user
        User user = authService.authenticate("admin", "admin123");
        assertNotNull(user, "Admin user should not be null on valid login");
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    @Order(2)
    @DisplayName("Valid staff login should return Staff user")
    void testValidStaffLogin() {
        User user = authService.authenticate("staff", "staff123");
        assertNotNull(user, "Staff user should not be null on valid login");
        assertEquals("staff", user.getUsername());
        assertEquals("STAFF", user.getRole());
    }

    @Test
    @Order(3)
    @DisplayName("Invalid password should return null")
    void testInvalidPassword() {
        User user = authService.authenticate("admin", "wrongpassword");
        assertNull(user, "User should be null with incorrect password");
    }

    @Test
    @Order(4)
    @DisplayName("Non-existent user should return null")
    void testNonExistentUser() {
        User user = authService.authenticate("unknown_user", "password");
        assertNull(user, "User should be null for non-existent account");
    }

    @Test
    @Order(5)
    @DisplayName("Empty username should return null")
    void testEmptyUsername() {
        User user = authService.authenticate("", "password");
        assertNull(user, "User should be null with empty username");
    }

    @Test
    @Order(6)
    @DisplayName("Empty password should return null")
    void testEmptyPassword() {
        User user = authService.authenticate("admin", "");
        assertNull(user, "User should be null with empty password");
    }

    @Test
    @Order(7)
    @DisplayName("Null credentials should return null")
    void testNullCredentials() {
        User user = authService.authenticate(null, null);
        assertNull(user, "User should be null with null credentials ");
    }
}
