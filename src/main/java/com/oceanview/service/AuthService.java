package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Authentication Service – handles login validation.
 * Note: State management is now handled by Servlets (HttpSession).
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Validates credentials and returns the User object if successful.
     * Returns null if invalid.
     */
    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());
        if (user != null) {
            String stored = user.getPassword();
            if (stored != null) {
                // Handle BCrypt-hashed passwords (start with $2a$, $2b$, $2x$, $2y$)
                if (stored.matches("^\\$2[aby]\\$.*")) {
                    if (BCrypt.checkpw(password.trim(), stored)) {
                        return user;
                    }
                } else {
                    // Legacy plain text password comparison (for backward compatibility)
                    if (stored.equals(password.trim())) {
                        return user;
                    }
                }
            }
        }

        return null; // Invalid credentials
    }
}
