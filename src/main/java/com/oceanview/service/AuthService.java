package com.oceanview.service;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.model.Guest;
import com.oceanview.model.User;

/**
 * Authentication Service – handles login validation.
 */
public class AuthService {

    private final UserDAO userDAO;
    private final GuestDAO guestDAO;
    private static String loggedInUser;
    private static String loggedInRole;
    private static Integer loggedInGuestId;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.guestDAO = new GuestDAO();
    }

    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        boolean valid = userDAO.validateCredentials(username.trim(), password.trim());
        if (valid) {
            User user = userDAO.findByUsername(username.trim());
            loggedInUser = username.trim();
            loggedInRole = user.getRole();
            loggedInGuestId = user.getGuestId();
        }
        return valid;
    }

    public static void logout() {
        loggedInUser = null;
        loggedInRole = null;
        loggedInGuestId = null;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    public static String getLoggedInRole() {
        return loggedInRole;
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(loggedInRole);
    }

    public static boolean isCustomer() {
        return "CUSTOMER".equalsIgnoreCase(loggedInRole);
    }

    public static Integer getLoggedInGuestId() {
        return loggedInGuestId;
    }

    public String registerCustomer(String name, String address, String contact, String username, String password) {
        if (name.isBlank() || address.isBlank() || contact.isBlank() || username.isBlank() || password.isBlank()) {
            return "All fields are required.";
        }
        if (userDAO.findByUsername(username) != null) {
            return "Username already exists.";
        }

        // 1. Create Guest entry
        Guest guest = new Guest(name, address, contact);
        int guestId = guestDAO.insertGuest(guest);
        if (guestId == -1)
            return "Error creating guest profile.";

        // 2. Create User entry
        User user = new User(0, username, password, "CUSTOMER", guestId);
        boolean success = userDAO.insertUser(user);

        return success ? "SUCCESS" : "Error creating user account.";
    }
}
