package com.oceanview.dao;

import com.oceanview.db.DatabaseConnection;
import com.oceanview.model.User;

import java.sql.*;

public class UserDAO {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getObject("guest_id") != null ? rs.getInt("guest_id") : null);
            }
        } catch (SQLException e) {
            System.err.println("UserDAO error: " + e.getMessage());
        }
        return null;
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Statement stmt = getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("UserDAO getAllUsers error: " + e.getMessage());
        }
        return users;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password, role, guest_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            if (user.getGuestId() != null) {
                ps.setInt(4, user.getGuestId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO insert error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET password = ?, role = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole());
            ps.setInt(3, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO update error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO delete error: " + e.getMessage());
        }
        return false;
    }

    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public String getUserRole(String username) {
        User user = findByUsername(username);
        return user != null ? user.getRole() : "STAFF";
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getObject("guest_id") != null ? rs.getInt("guest_id") : null);
    }
}
