package com.oceanview.dao;

import com.oceanview.db.DatabaseConnection;
import com.oceanview.model.Guest;

import java.sql.*;

public class GuestDAO {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public int insertGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, address, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getAddress());
            ps.setString(3, guest.getContactNumber());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("GuestDAO insert error: " + e.getMessage());
        }
        return -1;
    }

    public Guest findById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Guest(rs.getInt("id"), rs.getString("name"),
                    rs.getString("address"), rs.getString("contact_number"));
            }
        } catch (SQLException e) {
            System.err.println("GuestDAO findById error: " + e.getMessage());
        }
        return null;
    }
}
