package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.model.Room;
import com.oceanview.model.User;
import com.oceanview.util.Validator;

import java.util.List;

/**
 * AdminService – handles administrative CRUD operations for Rooms and Users.
 */
public class AdminService {

    private final RoomDAO roomDAO;
    private final UserDAO userDAO;

    public AdminService() {
        this.roomDAO = new RoomDAO();
        this.userDAO = new UserDAO();
    }

    // ── Room Management ───────────────────────────────────────────────────────

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public String saveRoom(Integer id, String roomNumber, String type, double rate) {
        if (!Validator.isNotEmpty(roomNumber))
            return "Room number is required.";
        if (!Validator.isNotEmpty(type))
            return "Room type is required.";
        if (rate <= 0)
            return "Rate must be greater than zero.";

        Room room = new Room(id != null ? id : 0, roomNumber.trim(), type.trim(), rate, "AVAILABLE");

        boolean success;
        if (id == null) {
            success = roomDAO.insertRoom(room);
        } else {
            success = roomDAO.updateRoom(room);
        }

        return success ? "SUCCESS" : "Failed to save room details.";
    }

    public boolean deleteRoom(int id) {
        return roomDAO.deleteRoom(id);
    }

    // ── User Management ───────────────────────────────────────────────────────

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public String saveUser(Integer id, String username, String password, String role) {
        if (!Validator.isNotEmpty(username))
            return "Username is required.";
        if (!Validator.isNotEmpty(password))
            return "Password is required.";
        if (!Validator.isNotEmpty(role))
            return "Role is required.";

        User user = new User(id != null ? id : 0, username.trim(), password.trim(), role.toUpperCase());

        boolean success;
        if (id == null) {
            success = userDAO.insertUser(user);
        } else {
            success = userDAO.updateUser(user);
        }

        return success ? "SUCCESS" : "Failed to save user account.";
    }

    public String deleteUser(int id, String currentUsername) {
        User userToDelete = userDAO.getAllUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (userToDelete != null && userToDelete.getUsername().equals(currentUsername)) {
            return "You cannot delete your own account while logged in.";
        }

        return userDAO.deleteUser(id) ? "SUCCESS" : "Failed to delete user account.";
    }
}
